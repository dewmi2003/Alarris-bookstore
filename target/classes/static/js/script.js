/**
 * ========================================
 * BOOKSTORE - COMPLETE JAVASCRIPT
 * Modern, Interactive Functionality
 * ========================================
 */

document.addEventListener('DOMContentLoaded', function() {
    
    // Initialize all features
    initMobileMenu();
    initAlertAutoDismiss();
    initFormValidation();
    initQuantityControls();
    initImagePreview();
    initSearchEnhancement();
    initAnimations();
    initBackToTop();
    
    console.log('📚 BookStore initialized successfully!');
});

// ============ MOBILE MENU ============
function initMobileMenu() {
    const toggle = document.getElementById('mobileMenuToggle');
    const menu = document.getElementById('navMenu');
    
    if (toggle && menu) {
        toggle.addEventListener('click', function() {
            menu.classList.toggle('active');
            toggle.classList.toggle('active');
        });
        
        // Close menu when clicking outside
        document.addEventListener('click', function(e) {
            if (!toggle.contains(e.target) && !menu.contains(e.target)) {
                menu.classList.remove('active');
                toggle.classList.remove('active');
            }
        });
    }
}

// ============ ALERT AUTO-DISMISS ============
function initAlertAutoDismiss() {
    const alerts = document.querySelectorAll('.alert');
    
    alerts.forEach(alert => {
        // Add close button
        const closeBtn = document.createElement('button');
        closeBtn.innerHTML = '×';
        closeBtn.style.cssText = `
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: inherit;
            opacity: 0.7;
            transition: opacity 0.3s;
        `;
        closeBtn.onmouseover = () => closeBtn.style.opacity = '1';
        closeBtn.onmouseout = () => closeBtn.style.opacity = '0.7';
        closeBtn.onclick = () => dismissAlert(alert);
        
        alert.style.position = 'relative';
        alert.style.paddingRight = '50px';
        alert.appendChild(closeBtn);
        
        // Auto-dismiss after 5 seconds
        setTimeout(() => dismissAlert(alert), 5000);
    });
}

function dismissAlert(alert) {
    alert.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
    alert.style.opacity = '0';
    alert.style.transform = 'translateY(-10px)';
    setTimeout(() => alert.remove(), 300);
}

// ============ FORM VALIDATION ============
function initFormValidation() {
    const forms = document.querySelectorAll('form');
    
    forms.forEach(form => {
        // Real-time validation
        const inputs = form.querySelectorAll('input, textarea, select');
        inputs.forEach(input => {
            input.addEventListener('blur', () => validateField(input));
            input.addEventListener('input', () => {
                if (input.classList.contains('is-invalid')) {
                    validateField(input);
                }
            });
        });
        
        // Form submit validation
        form.addEventListener('submit', function(e) {
            let isValid = true;
            
            inputs.forEach(input => {
                if (!validateField(input)) {
                    isValid = false;
                }
            });
            
            // Password confirmation check
            const password = form.querySelector('input[name="password"]');
            const confirmPassword = form.querySelector('input[name="confirmPassword"]');
            
            if (password && confirmPassword && password.value !== confirmPassword.value) {
                showFieldError(confirmPassword, 'Passwords do not match');
                isValid = false;
            }
            
            if (!isValid) {
                e.preventDefault();
                showNotification('Please fix the errors in the form', 'error');
            }
        });
    });
}

function validateField(field) {
    const value = field.value.trim();
    const isRequired = field.hasAttribute('required');
    
    removeFieldError(field);
    field.classList.remove('is-invalid');
    
    // Required validation
    if (isRequired && !value) {
        showFieldError(field, 'This field is required');
        field.classList.add('is-invalid');
        return false;
    }
    
    // Email validation
    if (field.type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            showFieldError(field, 'Please enter a valid email address');
            field.classList.add('is-invalid');
            return false;
        }
    }
    
    // Min length validation
    const minLength = field.getAttribute('minlength');
    if (minLength && value.length < minLength) {
        showFieldError(field, `Minimum ${minLength} characters required`);
        field.classList.add('is-invalid');
        return false;
    }
    
    // Number validation
    if (field.type === 'number') {
        const min = field.getAttribute('min');
        const max = field.getAttribute('max');
        const numValue = parseFloat(value);
        
        if (min && numValue < parseFloat(min)) {
            showFieldError(field, `Minimum value is ${min}`);
            field.classList.add('is-invalid');
            return false;
        }
        
        if (max && numValue > parseFloat(max)) {
            showFieldError(field, `Maximum value is ${max}`);
            field.classList.add('is-invalid');
            return false;
        }
    }
    
    field.classList.add('is-valid');
    return true;
}

function showFieldError(field, message) {
    removeFieldError(field);
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'field-error-message';
    errorDiv.textContent = message;
    errorDiv.style.cssText = `
        color: #e74c3c;
        font-size: 13px;
        margin-top: 5px;
        animation: slideDown 0.3s ease;
    `;
    
    field.parentNode.appendChild(errorDiv);
}

function removeFieldError(field) {
    const existingError = field.parentNode.querySelector('.field-error-message');
    if (existingError) {
        existingError.remove();
    }
}

// ============ QUANTITY CONTROLS ============
function initQuantityControls() {
    const quantityInputs = document.querySelectorAll('input[type="number"][name="quantity"]');
    
    quantityInputs.forEach(input => {
        // Ensure valid values
        input.addEventListener('input', function() {
            const min = parseInt(input.getAttribute('min')) || 1;
            const max = parseInt(input.getAttribute('max')) || 999;
            let value = parseInt(input.value);
            
            if (isNaN(value) || value < min) {
                input.value = min;
            } else if (value > max) {
                input.value = max;
                showNotification(`Maximum quantity is ${max}`, 'warning');
            }
        });
        
        // Add increment/decrement buttons if not already present
        if (!input.parentNode.classList.contains('quantity-wrapper')) {
            wrapQuantityInput(input);
        }
    });
}

function wrapQuantityInput(input) {
    const wrapper = document.createElement('div');
    wrapper.className = 'quantity-controls';
    wrapper.style.cssText = `
        display: inline-flex;
        align-items: center;
        gap: 10px;
        border: 2px solid #e0e0e0;
        border-radius: 10px;
        padding: 5px;
        background: white;
    `;
    
    const decrementBtn = createQuantityButton('-', () => {
        const min = parseInt(input.getAttribute('min')) || 1;
        const current = parseInt(input.value) || min;
        if (current > min) {
            input.value = current - 1;
            input.dispatchEvent(new Event('change', { bubbles: true }));
        }
    });
    
    const incrementBtn = createQuantityButton('+', () => {
        const max = parseInt(input.getAttribute('max')) || 999;
        const current = parseInt(input.value) || 1;
        if (current < max) {
            input.value = current + 1;
            input.dispatchEvent(new Event('change', { bubbles: true }));
        }
    });
    
    input.style.cssText = `
        width: 60px;
        text-align: center;
        border: none;
        font-size: 16px;
        font-weight: bold;
    `;
    
    input.parentNode.insertBefore(wrapper, input);
    wrapper.appendChild(decrementBtn);
    wrapper.appendChild(input);
    wrapper.appendChild(incrementBtn);
}

function createQuantityButton(text, onClick) {
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.textContent = text;
    btn.style.cssText = `
        width: 30px;
        height: 30px;
        border: none;
        background: #ffb703;
        color: #1e1e2f;
        border-radius: 5px;
        font-size: 18px;
        font-weight: bold;
        cursor: pointer;
        transition: all 0.3s ease;
    `;
    btn.onmouseover = () => btn.style.background = '#ffd966';
    btn.onmouseout = () => btn.style.background = '#ffb703';
    btn.onclick = onClick;
    return btn;
}

// ============ IMAGE PREVIEW ============
function initImagePreview() {
    const fileInputs = document.querySelectorAll('input[type="file"][accept*="image"]');
    
    fileInputs.forEach(input => {
        input.addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file && file.type.startsWith('image/')) {
                const reader = new FileReader();
                
                reader.onload = function(event) {
                    let preview = input.parentNode.querySelector('.image-preview');
                    
                    if (!preview) {
                        preview = document.createElement('div');
                        preview.className = 'image-preview';
                        preview.style.cssText = `
                            margin-top: 15px;
                            text-align: center;
                        `;
                        input.parentNode.appendChild(preview);
                    }
                    
                    preview.innerHTML = `
                        <img src="${event.target.result}" 
                             alt="Preview" 
                             style="max-width: 300px; border-radius: 10px; box-shadow: 0 5px 15px rgba(0,0,0,0.1);">
                        <p style="margin-top: 10px; color: #666; font-size: 14px;">${file.name}</p>
                    `;
                };
                
                reader.readAsDataURL(file);
            }
        });
    });
}

// ============ SEARCH ENHANCEMENT ============
function initSearchEnhancement() {
    const searchInputs = document.querySelectorAll('input[type="text"][placeholder*="Search"]');
    
    searchInputs.forEach(input => {
        // Add search icon
        input.style.paddingLeft = '40px';
        input.style.backgroundImage = 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\' width=\'20\' height=\'20\' viewBox=\'0 0 24 24\' fill=\'none\' stroke=\'%23666\' stroke-width=\'2\'%3E%3Ccircle cx=\'11\' cy=\'11\' r=\'8\'/%3E%3Cpath d=\'m21 21-4.35-4.35\'/%3E%3C/svg%3E")';
        input.style.backgroundRepeat = 'no-repeat';
        input.style.backgroundPosition = '12px center';
        
        // Add clear button
        const clearBtn = document.createElement('button');
        clearBtn.type = 'button';
        clearBtn.innerHTML = '×';
        clearBtn.style.cssText = `
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            font-size: 24px;
            cursor: pointer;
            color: #999;
            display: none;
            transition: color 0.3s;
        `;
        clearBtn.onmouseover = () => clearBtn.style.color = '#333';
        clearBtn.onmouseout = () => clearBtn.style.color = '#999';
        clearBtn.onclick = () => {
            input.value = '';
            clearBtn.style.display = 'none';
            input.focus();
            input.dispatchEvent(new Event('input', { bubbles: true }));
        };
        
        input.parentNode.style.position = 'relative';
        input.parentNode.appendChild(clearBtn);
        
        input.addEventListener('input', function() {
            clearBtn.style.display = this.value ? 'block' : 'none';
        });
        
        if (input.value) {
            clearBtn.style.display = 'block';
        }
    });
}

// ============ SCROLL ANIMATIONS ============
function initAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -100px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
                observer.unobserve(entry.target);
            }
        });
    }, observerOptions);
    
    const animatedElements = document.querySelectorAll('.book-card, .admin-card, .order-card');
    animatedElements.forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(30px)';
        el.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
        observer.observe(el);
    });
}

// ============ BACK TO TOP BUTTON ============
function initBackToTop() {
    const backToTopBtn = document.createElement('button');
    backToTopBtn.innerHTML = '↑';
    backToTopBtn.className = 'back-to-top';
    backToTopBtn.style.cssText = `
        position: fixed;
        bottom: 30px;
        right: 30px;
        width: 50px;
        height: 50px;
        background: #ffb703;
        color: #1e1e2f;
        border: none;
        border-radius: 50%;
        font-size: 24px;
        font-weight: bold;
        cursor: pointer;
        display: none;
        z-index: 999;
        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        transition: all 0.3s ease;
    `;
    
    backToTopBtn.onmouseover = () => {
        backToTopBtn.style.background = '#ffd966';
        backToTopBtn.style.transform = 'translateY(-5px)';
    };
    backToTopBtn.onmouseout = () => {
        backToTopBtn.style.background = '#ffb703';
        backToTopBtn.style.transform = 'translateY(0)';
    };
    
    backToTopBtn.onclick = () => {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };
    
    document.body.appendChild(backToTopBtn);
    
    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            backToTopBtn.style.display = 'block';
        } else {
            backToTopBtn.style.display = 'none';
        }
    });
}

// ============ NOTIFICATION SYSTEM ============
function showNotification(message, type = 'info') {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    
    const icons = {
        success: '✅',
        error: '⚠️',
        warning: '⚡',
        info: 'ℹ️'
    };
    
    const colors = {
        success: { bg: '#d4edda', border: '#c3e6cb', text: '#155724' },
        error: { bg: '#f8d7da', border: '#f5c6cb', text: '#721c24' },
        warning: { bg: '#fff3cd', border: '#ffeaa7', text: '#856404' },
        info: { bg: '#d1ecf1', border: '#bee5eb', text: '#0c5460' }
    };
    
    const color = colors[type] || colors.info;
    
    notification.style.cssText = `
        position: fixed;
        top: 80px;
        right: -400px;
        background: ${color.bg};
        color: ${color.text};
        padding: 15px 20px;
        border-radius: 10px;
        box-shadow: 0 5px 20px rgba(0,0,0,0.2);
        border: 1px solid ${color.border};
        z-index: 10000;
        min-width: 300px;
        transition: right 0.3s ease;
        display: flex;
        align-items: center;
        gap: 10px;
    `;
    
    notification.innerHTML = `
        <span style="font-size: 20px;">${icons[type] || icons.info}</span>
        <span>${message}</span>
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => notification.style.right = '20px', 10);
    
    setTimeout(() => {
        notification.style.right = '-400px';
        setTimeout(() => notification.remove(), 300);
    }, 3000);
}

// ============ CONFIRM DIALOGS ============
function confirmAction(message, callback) {
    const overlay = document.createElement('div');
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10000;
        animation: fadeIn 0.3s ease;
    `;
    
    const dialog = document.createElement('div');
    dialog.style.cssText = `
        background: white;
        padding: 30px;
        border-radius: 15px;
        max-width: 400px;
        text-align: center;
        box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        animation: slideIn 0.3s ease;
    `;
    
    dialog.innerHTML = `
        <h3 style="margin-bottom: 20px; color: #1e1e2f;">Confirm Action</h3>
        <p style="margin-bottom: 25px; color: #666;">${message}</p>
        <div style="display: flex; gap: 10px; justify-content: center;">
            <button class="btn-cancel" style="padding: 10px 20px; background: #6c757d; color: white; border: none; border-radius: 8px; cursor: pointer;">Cancel</button>
            <button class="btn-confirm" style="padding: 10px 20px; background: #e74c3c; color: white; border: none; border-radius: 8px; cursor: pointer;">Confirm</button>
        </div>
    `;
    
    overlay.appendChild(dialog);
    document.body.appendChild(overlay);
    
    dialog.querySelector('.btn-cancel').onclick = () => overlay.remove();
    dialog.querySelector('.btn-confirm').onclick = () => {
        callback();
        overlay.remove();
    };
}

// ============ LOADING SPINNER ============
function showLoading() {
    const loader = document.createElement('div');
    loader.id = 'globalLoader';
    loader.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.7);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10000;
    `;
    
    loader.innerHTML = `
        <div style="
            width: 60px;
            height: 60px;
            border: 5px solid rgba(255,183,3,0.3);
            border-top-color: #ffb703;
            border-radius: 50%;
            animation: spin 1s linear infinite;
        "></div>
    `;
    
    document.body.appendChild(loader);
}

function hideLoading() {
    const loader = document.getElementById('globalLoader');
    if (loader) loader.remove();
}

// ============ UTILITY FUNCTIONS ============
function formatCurrency(amount) {
    return '$' + parseFloat(amount).toFixed(2);
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func(...args), wait);
    };
}

// ============ DELETE CONFIRMATIONS ============
document.addEventListener('click', function(e) {
    if (e.target.closest('button.btn-danger') || 
        e.target.closest('form[action*="delete"]')) {
        
        const form = e.target.closest('form');
        if (form) {
            e.preventDefault();
            confirmAction('Are you sure you want to delete this item? This action cannot be undone.', () => {
                form.submit();
            });
        }
    }
});

// ============ SMOOTH SCROLL ============
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        const href = this.getAttribute('href');
        if (href === '#') return;
        
        const target = document.querySelector(href);
        if (target) {
            e.preventDefault();
            target.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    });
});

// ============ EXPOSE GLOBAL FUNCTIONS ============
window.showNotification = showNotification;
window.confirmAction = confirmAction;
window.showLoading = showLoading;
window.hideLoading = hideLoading;