class HamburgerMenu {
    constructor() {
        this.button = document.querySelector('.hamburger-button');
        this.nav = document.querySelector('.hamburger-nav');
        this.overlay = document.querySelector('.menu-overlay');
        this.closeButton = document.querySelector('.close-menu');
        this.bindEvents();
    }

    bindEvents() {
        this.button.addEventListener('click', () => this.openMenu());
        this.closeButton.addEventListener('click', () => this.closeMenu());
        this.overlay.addEventListener('click', () => this.closeMenu());
    }

    openMenu() {
        document.body.classList.add('menu-open');
    }

    closeMenu() {
        document.body.classList.remove('menu-open');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    new HamburgerMenu();
});