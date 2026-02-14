// ========================================
// ALARIIS BOOKSTORE - GLOBAL DESIGN SYSTEM JS
// Consistent Interactions Across All Pages
// ========================================

(function () {
    'use strict';

    // ============ THEME MANAGER ============
    const ThemeManager = {
        init() {
            this.themeToggle = document.getElementById('themeToggle');
            this.html = document.documentElement;

            // Load saved theme
            const savedTheme = localStorage.getItem('theme') || 'light';
            this.setTheme(savedTheme);

            // Add event listener
            if (this.themeToggle) {
                this.themeToggle.addEventListener('click', () => this.toggleTheme());
            }
        },

        setTheme(theme) {
            this.html.setAttribute('data-theme', theme);
            localStorage.setItem('theme', theme);
            this.updateIcon(theme);
        },

        toggleTheme() {
            const currentTheme = this.html.getAttribute('data-theme');
            const newTheme = currentTheme === 'light' ? 'dark' : 'light';
            this.setTheme(newTheme);
        },

        updateIcon(theme) {
            if (!this.themeToggle) return;
            const icon = this.themeToggle.querySelector('i');
            if (icon) {
                icon.className = theme === 'light' ? 'fas fa-moon' : 'fas fa-sun';
            }
        }
    };

    // ============ TOAST NOTIFICATIONS ============
    const Toast = {
        container: null,

        init() {
            // Create toast container if it doesn't exist
            if (!document.querySelector('.toast-container')) {
                this.container = document.createElement('div');
                this.container.className = 'toast-container';
                document.body.appendChild(this.container);
            } else {
                this.container = document.querySelector('.toast-container');
            }
        },

        show(message, type = 'info', duration = 3000) {
            const toast = document.createElement('div');
            toast.className = `toast toast-${type}`;

            const icons = {
                success: 'fas fa-check-circle',
                error: 'fas fa-times-circle',
                warning: 'fas fa-exclamation-triangle',
                info: 'fas fa-info-circle'
            };

            toast.innerHTML = `
                <i class="toast-icon ${icons[type]}"></i>
                <div class="toast-content">
                    <div class="toast-message">${message}</div>
                </div>
                <button class="toast-close" onclick="this.parentElement.remove()">
                    <i class="fas fa-times"></i>
                </button>
            `;

            this.container.appendChild(toast);

            // Auto remove after duration
            setTimeout(() => {
                toast.style.opacity = '0';
                toast.style.transform = 'translateX(100%)';
                setTimeout(() => toast.remove(), 300);
            }, duration);
        },

        success(message, duration) {
            this.show(message, 'success', duration);
        },

        error(message, duration) {
            this.show(message, 'error', duration);
        },

        warning(message, duration) {
            this.show(message, 'warning', duration);
        },

        info(message, duration) {
            this.show(message, 'info', duration);
        }
    };

    // ============ SCROLL PROGRESS ============
    const ScrollProgress = {
        init() {
            this.progressBar = document.getElementById('scrollProgress');
            if (!this.progressBar) {
                this.progressBar = document.createElement('div');
                this.progressBar.className = 'scroll-progress';
                this.progressBar.id = 'scrollProgress';
                document.body.prepend(this.progressBar);
            }

            window.addEventListener('scroll', () => this.update());
        },

        update() {
            const scrollHeight = document.documentElement.scrollHeight - window.innerHeight;
            const scrolled = (window.scrollY / scrollHeight) * 100;
            this.progressBar.style.width = scrolled + '%';
        }
    };

    // ============ BACK TO TOP BUTTON ============
    const BackToTop = {
        init() {
            this.button = document.getElementById('backToTop');
            if (!this.button) {
                this.button = document.createElement('button');
                this.button.className = 'back-to-top';
                this.button.id = 'backToTop';
                this.button.innerHTML = '<i class="fas fa-arrow-up"></i>';
                document.body.appendChild(this.button);
            }

            window.addEventListener('scroll', () => this.toggle());
            this.button.addEventListener('click', () => this.scrollToTop());
        },

        toggle() {
            if (window.scrollY > 500) {
                this.button.classList.add('visible');
            } else {
                this.button.classList.remove('visible');
            }
        },

        scrollToTop() {
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    };

    // ============ MODAL MANAGER ============
    const Modal = {
        open(modalId) {
            const modal = document.getElementById(modalId);
            if (modal) {
                modal.classList.add('active');
                document.body.style.overflow = 'hidden';
            }
        },

        close(modalId) {
            const modal = document.getElementById(modalId);
            if (modal) {
                modal.classList.remove('active');
                document.body.style.overflow = '';
            }
        },

        init() {
            // Close modal on backdrop click
            document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
                backdrop.addEventListener('click', (e) => {
                    if (e.target === backdrop) {
                        backdrop.classList.remove('active');
                        document.body.style.overflow = '';
                    }
                });
            });

            // Close modal on close button click
            document.querySelectorAll('.modal-close').forEach(btn => {
                btn.addEventListener('click', () => {
                    const modal = btn.closest('.modal-backdrop');
                    if (modal) {
                        modal.classList.remove('active');
                        document.body.style.overflow = '';
                    }
                });
            });
        }
    };

    // ============ TAB MANAGER ============
    const Tabs = {
        init() {
            document.querySelectorAll('.tab').forEach(tab => {
                tab.addEventListener('click', () => this.switchTab(tab));
            });
        },

        switchTab(clickedTab) {
            const tabGroup = clickedTab.closest('.tabs');
            const targetId = clickedTab.dataset.tab;

            // Remove active class from all tabs in this group
            tabGroup.querySelectorAll('.tab').forEach(tab => {
                tab.classList.remove('active');
            });

            // Add active class to clicked tab
            clickedTab.classList.add('active');

            // Hide all tab contents
            const container = tabGroup.parentElement;
            container.querySelectorAll('.tab-content').forEach(content => {
                content.classList.remove('active');
            });

            // Show target content
            const targetContent = document.getElementById(targetId);
            if (targetContent) {
                targetContent.classList.add('active');
            }
        }
    };

    // ============ DROPDOWN MANAGER ============
    const Dropdown = {
        init() {
            // Close dropdowns when clicking outside
            document.addEventListener('click', (e) => {
                if (!e.target.closest('.dropdown')) {
                    document.querySelectorAll('.dropdown-menu.active').forEach(menu => {
                        menu.classList.remove('active');
                    });
                }
            });
        },

        toggle(dropdownId) {
            const menu = document.getElementById(dropdownId);
            if (menu) {
                menu.classList.toggle('active');
            }
        }
    };

    // ============ FORM VALIDATION ============
    const FormValidator = {
        init() {
            document.querySelectorAll('form[data-validate]').forEach(form => {
                form.addEventListener('submit', (e) => this.validate(e, form));
            });

            // Real-time validation
            document.querySelectorAll('input[required], textarea[required]').forEach(input => {
                input.addEventListener('blur', () => this.validateField(input));
            });
        },

        validate(e, form) {
            let isValid = true;
            const inputs = form.querySelectorAll('input[required], textarea[required], select[required]');

            inputs.forEach(input => {
                if (!this.validateField(input)) {
                    isValid = false;
                }
            });

            if (!isValid) {
                e.preventDefault();
                Toast.error('Please fill in all required fields');
            }

            return isValid;
        },

        validateField(input) {
            const value = input.value.trim();
            const type = input.type;
            let isValid = true;

            // Required check
            if (input.hasAttribute('required') && !value) {
                isValid = false;
            }

            // Email validation
            if (type === 'email' && value) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                isValid = emailRegex.test(value);
            }

            // Update UI
            if (isValid) {
                input.classList.remove('input-error');
            } else {
                input.classList.add('input-error');
            }

            return isValid;
        }
    };

    // ============ SCROLL ANIMATIONS ============
    const ScrollAnimations = {
        init() {
            const observer = new IntersectionObserver(
                (entries) => {
                    entries.forEach(entry => {
                        if (entry.isIntersecting) {
                            entry.target.style.opacity = '1';
                            entry.target.style.transform = 'translateY(0)';
                        }
                    });
                },
                { threshold: 0.1, rootMargin: '0px 0px -50px 0px' }
            );

            // Observe elements with scroll-animate class
            document.querySelectorAll('.scroll-animate').forEach(el => {
                el.style.opacity = '0';
                el.style.transform = 'translateY(30px)';
                el.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                observer.observe(el);
            });
        }
    };

    // ============ LAZY LOADING IMAGES ============
    const LazyLoad = {
        init() {
            const imageObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        const img = entry.target;
                        if (img.dataset.src) {
                            img.src = img.dataset.src;
                            img.removeAttribute('data-src');
                            img.classList.add('loaded');
                        }
                        imageObserver.unobserve(img);
                    }
                });
            });

            document.querySelectorAll('img[data-src]').forEach(img => {
                imageObserver.observe(img);
            });
        }
    };

    // ============ SMOOTH SCROLL FOR ANCHORS ============
    const SmoothScroll = {
        init() {
            document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                anchor.addEventListener('click', function (e) {
                    const href = this.getAttribute('href');
                    if (href !== '#' && href !== '') {
                        e.preventDefault();
                        const target = document.querySelector(href);
                        if (target) {
                            target.scrollIntoView({
                                behavior: 'smooth',
                                block: 'start'
                            });
                        }
                    }
                });
            });
        }
    };

    // ============ COUNTDOWN TIMER ============
    const Countdown = {
        timers: new Map(),

        init() {
            document.querySelectorAll('[data-countdown]').forEach(el => {
                const targetTime = new Date(el.dataset.countdown).getTime();
                this.start(el, targetTime);
            });
        },

        start(element, targetTime) {
            const update = () => {
                const now = new Date().getTime();
                const distance = targetTime - now;

                if (distance < 0) {
                    element.innerHTML = 'EXPIRED';
                    return;
                }

                const days = Math.floor(distance / (1000 * 60 * 60 * 24));
                const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((distance % (1000 * 60)) / 1000);

                const hoursEl = element.querySelector('[data-hours]');
                const minutesEl = element.querySelector('[data-minutes]');
                const secondsEl = element.querySelector('[data-seconds]');

                if (hoursEl) hoursEl.textContent = String(hours).padStart(2, '0');
                if (minutesEl) minutesEl.textContent = String(minutes).padStart(2, '0');
                if (secondsEl) secondsEl.textContent = String(seconds).padStart(2, '0');
            };

            update();
            const timerId = setInterval(update, 1000);
            this.timers.set(element, timerId);
        }
    };

    // ============ DEBOUNCE UTILITY ============
    function debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // ============ INITIALIZE ALL MODULES ============
    function init() {
        ThemeManager.init();
        Toast.init();
        ScrollProgress.init();
        BackToTop.init();
        Modal.init();
        Tabs.init();
        Dropdown.init();
        FormValidator.init();
        ScrollAnimations.init();
        LazyLoad.init();
        SmoothScroll.init();
        Countdown.init();

        console.log('%c🎨 Alariis Design System Loaded', 'color: #1E2A78; font-size: 16px; font-weight: bold;');
    }

    // Initialize when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // Expose utilities globally
    window.AlariisDesignSystem = {
        Toast,
        Modal,
        Dropdown,
        ThemeManager,
        debounce
    };

})();
