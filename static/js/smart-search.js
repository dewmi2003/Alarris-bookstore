
(function () {
    'use strict';

    const SmartSearch = {
        searchInput: null,
        suggestionsContainer: null,
        debounceTimer: null,

        init() {
            this.searchInput = document.getElementById('globalSearch');
            this.suggestionsContainer = document.getElementById('searchSuggestions');

            if (!this.searchInput) return;

            
            this.searchInput.addEventListener('input', (e) => this.handleInput(e));
            this.searchInput.addEventListener('focus', () => this.showSuggestions());

            
            document.addEventListener('click', (e) => {
                if (!e.target.closest('.search-bar-wrapper')) {
                    this.hideSuggestions();
                }
            });
        },

        handleInput(e) {
            const query = e.target.value.trim();

           
            clearTimeout(this.debounceTimer);

            if (query.length < 2) {
                this.hideSuggestions();
                return;
            }

            
            this.debounceTimer = setTimeout(() => {
                this.fetchSuggestions(query);
            }, 300);
        },

        async fetchSuggestions(query) {
            try {
                // In production, this would call your backend API
                // For now, we'll use mock data
                const suggestions = this.getMockSuggestions(query);
                this.displaySuggestions(suggestions);
            } catch (error) {
                console.error('Error fetching suggestions:', error);
            }
        },

        getMockSuggestions(query) {
            // Mock data - in production, replace with actual API call
            const allBooks = [
                { id: 1, title: '1984', author: 'George Orwell', price: 13.99, image: '1984.jpg' },
                { id: 2, title: 'Dune', author: 'Frank Herbert', price: 18.99, image: 'dune.jpg' },
                { id: 3, title: 'The Hobbit', author: 'J.R.R. Tolkien', price: 16.99, image: 'hobbit.jpg' },
                { id: 4, title: 'Harry Potter', author: 'J.K. Rowling', price: 17.99, image: 'harry-potter-1.jpg' },
                { id: 5, title: 'Sapiens', author: 'Yuval Noah Harari', price: 19.99, image: 'sapiens.jpg' }
            ];

            const lowerQuery = query.toLowerCase();
            return allBooks.filter(book =>
                book.title.toLowerCase().includes(lowerQuery) ||
                book.author.toLowerCase().includes(lowerQuery)
            ).slice(0, 5);
        },

        displaySuggestions(suggestions) {
            if (!this.suggestionsContainer) return;

            if (suggestions.length === 0) {
                this.suggestionsContainer.innerHTML = `
                    <div class="no-suggestions">
                        <i class="fas fa-search"></i>
                        <p>No books found matching your search</p>
                    </div>
                `;
                this.showSuggestions();
                return;
            }

            const html = suggestions.map(book => `
                <a href="/book/${book.id}" class="search-suggestion-item">
                    ${book.image ?
                    `<img src="/images/books/${book.image}" alt="${book.title}" class="suggestion-image">` :
                    `<div class="suggestion-placeholder"><i class="fas fa-book"></i></div>`
                }
                    <div class="suggestion-details">
                        <div class="suggestion-title">${this.highlightMatch(book.title, this.searchInput.value)}</div>
                        <div class="suggestion-author">${book.author}</div>
                    </div>
                    <div class="suggestion-price">$${book.price.toFixed(2)}</div>
                </a>
            `).join('');

            this.suggestionsContainer.innerHTML = html;
            this.showSuggestions();
        },

        highlightMatch(text, query) {
            const regex = new RegExp(`(${query})`, 'gi');
            return text.replace(regex, '<strong>$1</strong>');
        },

        showSuggestions() {
            if (this.suggestionsContainer) {
                this.suggestionsContainer.style.display = 'block';
            }
        },

        hideSuggestions() {
            if (this.suggestionsContainer) {
                this.suggestionsContainer.style.display = 'none';
            }
        }
    };

   

    const VoiceSearch = {
        recognition: null,
        isListening: false,
        voiceBtn: null,

        init() {
            this.voiceBtn = document.getElementById('voiceSearchBtn');
            if (!this.voiceBtn) return;

            
            if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
                this.voiceBtn.style.display = 'none';
                return;
            }

            
            const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
            this.recognition = new SpeechRecognition();
            this.recognition.continuous = false;
            this.recognition.interimResults = false;
            this.recognition.lang = 'en-US';

            
            this.voiceBtn.addEventListener('click', () => this.toggleListening());

            this.recognition.onresult = (event) => this.handleResult(event);
            this.recognition.onerror = (event) => this.handleError(event);
            this.recognition.onend = () => this.stopListening();
        },

        toggleListening() {
            if (this.isListening) {
                this.stopListening();
            } else {
                this.startListening();
            }
        },

        startListening() {
            this.isListening = true;
            this.voiceBtn.classList.add('voice-search-active');

           
            this.showVoiceModal();

            try {
                this.recognition.start();
            } catch (error) {
                console.error('Error starting voice recognition:', error);
                this.stopListening();
            }
        },

        stopListening() {
            this.isListening = false;
            this.voiceBtn.classList.remove('voice-search-active');
            this.hideVoiceModal();

            try {
                this.recognition.stop();
            } catch (error) {
                
            }
        },

        handleResult(event) {
            const transcript = event.results[0][0].transcript;

            
            const searchInput = document.getElementById('globalSearch');
            if (searchInput) {
                searchInput.value = transcript;

                
                const form = searchInput.closest('form');
                if (form) {
                    form.submit();
                }
            }

            this.stopListening();

            
            if (window.AlariisDesignSystem) {
                window.AlariisDesignSystem.Toast.success(`Searching for: "${transcript}"`);
            }
        },

        handleError(event) {
            console.error('Voice recognition error:', event.error);
            this.stopListening();

            if (window.AlariisDesignSystem) {
                window.AlariisDesignSystem.Toast.error('Voice search failed. Please try again.');
            }
        },

        showVoiceModal() {
           
            this.hideVoiceModal();

            const modal = document.createElement('div');
            modal.id = 'voiceSearchModal';
            modal.className = 'voice-search-modal';
            modal.innerHTML = `
                <i class="fas fa-microphone voice-icon-animated"></i>
                <div class="voice-text">Listening...</div>
                <div class="voice-text" style="font-size: var(--text-sm); color: var(--text-tertiary);">
                    Speak now to search for books
                </div>
                <button class="btn btn-outline btn-sm" onclick="document.getElementById('voiceSearchBtn').click()" style="margin-top: var(--space-3);">
                    Cancel
                </button>
            `;

            
            const backdrop = document.createElement('div');
            backdrop.id = 'voiceSearchBackdrop';
            backdrop.className = 'modal-backdrop active';
            backdrop.style.zIndex = 'var(--z-modal-backdrop)';

            document.body.appendChild(backdrop);
            document.body.appendChild(modal);

            
            backdrop.addEventListener('click', () => this.stopListening());
        },

        hideVoiceModal() {
            const modal = document.getElementById('voiceSearchModal');
            const backdrop = document.getElementById('voiceSearchBackdrop');

            if (modal) modal.remove();
            if (backdrop) backdrop.remove();
        }
    };

   
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            SmartSearch.init();
            VoiceSearch.init();
        });
    } else {
        SmartSearch.init();
        VoiceSearch.init();
    }

    
    window.SmartSearch = SmartSearch;
    window.VoiceSearch = VoiceSearch;

})();
