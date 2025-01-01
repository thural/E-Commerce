import { Modal } from './modal.js';
import { APIController } from './api.js';
import { CartController } from './cart.js';

class ProductDescriptionModal {
    constructor(productId) {
        this.productId = productId;
        this.modalId = `productModal-${productId}`;
        this.modal = Modal.init(this.modalId);
        this.currentSlide = 0;
        this.initializeComponents();
        this.attachEventListeners();
    }

    initializeComponents() {
        const modalElement = document.getElementById(this.modalId);
        this.track = modalElement.querySelector('.carousel-track');
        this.images = modalElement.querySelectorAll('.carousel-image');
        this.prevButton = modalElement.querySelector('.carousel-button.prev');
        this.nextButton = modalElement.querySelector('.carousel-button.next');
        this.setupCarouselDots();
    }

    setupCarouselDots() {
        const dotsContainer = this.modal.modal.querySelector('.carousel-dots');
        if (this.images.length > 1) {
            this.images.forEach((_, index) => {
                const dot = document.createElement('span');
                dot.classList.add('carousel-dot');
                if (index === 0) dot.classList.add('active');
                dot.addEventListener('click', () => this.goToSlide(index));
                dotsContainer.appendChild(dot);
            });
        }
    }

    attachEventListeners() {
        const modalElement = this.modal.modal;

        this.prevButton?.addEventListener('click', () => this.prevSlide());
        this.nextButton?.addEventListener('click', () => this.nextSlide());

        modalElement.querySelector('.add-to-cart-btn')?.addEventListener('click', e => {
            const button = e.target;
            CartController.handleCartAction(
                button.dataset.productId,
                'INCREMENT',
                0
            );
        });

        modalElement.querySelector('.add-to-wishlist-btn')?.addEventListener('click', e => {
            this.handleWishlist(e.target.dataset.productId);
        });
    }

    async handleWishlist(productId) {
        try {
            const response = await APIController.updateWishlist(productId);
            if (response.success) {
                const wishlistBtn = this.modal.modal.querySelector('.add-to-wishlist-btn');
                wishlistBtn.classList.toggle('active');
                wishlistBtn.textContent = wishlistBtn.classList.contains('active')
                    ? 'In Wishlist'
                    : 'Add to Wishlist';
            }
        } catch (error) {
            console.error('Wishlist update failed:', error);
        }
    }

    goToSlide(index) {
        this.currentSlide = index;
        this.updateCarousel();
    }

    prevSlide() {
        this.currentSlide = (this.currentSlide - 1 + this.images.length) % this.images.length;
        this.updateCarousel();
    }

    nextSlide() {
        this.currentSlide = (this.currentSlide + 1) % this.images.length;
        this.updateCarousel();
    }

    updateCarousel() {
        const offset = -this.currentSlide * 100;
        this.track.style.transform = `translateX(${offset}%)`;

        this.modal.modal.querySelectorAll('.carousel-dot').forEach((dot, index) => {
            dot.classList.toggle('active', index === this.currentSlide);
        });
    }

    open() {
        this.modal.open();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.product-card').forEach(card => {
        card.addEventListener('click', function(e) {
            if (!e.target.closest('button')) {
                const productId = this.getAttribute('id');
                const modal = new ProductDescriptionModal(productId);
                modal.open();
            }
        });
    });
});