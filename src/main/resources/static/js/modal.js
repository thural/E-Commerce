export class Modal {
    constructor(modalElement) {
        this.modal = modalElement;
        this.closeBtn = modalElement.querySelector('.modal-close');
        this.attachEventListeners();
    }

    attachEventListeners() {
        this.closeBtn.addEventListener('click', () => this.close());
        this.modal.addEventListener('click', (e) => {
            if (e.target === this.modal) this.close();
        });
    }

    open() {
        document.body.style.overflow = 'hidden';
        this.modal.classList.add('fade-in');
        this.modal.style.display = 'block';
    }

    close() {
        this.modal.classList.add('fade-out');
        setTimeout(() => {
            this.modal.style.display = 'none';
            this.modal.classList.remove('fade-out', 'fade-in');
            document.body.style.overflow = 'auto';
        }, 300);
    }

    static init(modalId) {
        const modalElement = document.getElementById(modalId);
        return new Modal(modalElement);
    }
}

// Initialize all modals
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.modal').forEach(modal => new Modal(modal));
});