import { APIController } from './api.js';
import { UIController } from './ui.js';

console.log("hello from CART JS");
export const CartController = {
    async handleCartAction(productId, action, currentCount) {
        console.log("itemId, action and currentCount on handleCartAction: ", productId, action, currentCount);
        if (action === 'DECREMENT' && currentCount <= 0) return;

        try {
            const data = await APIController.updateCart(productId, action);
            console.log("received data and itemId on handleCartAction: ", data, productId);
            UIController.updateCartUI(data, productId);
        } catch (error) {
            console.error('Cart action failed:', error);
        }
    },

    checkout() {
        window.location.href = '/checkout';
    },

    testFun() {
        console.log("CART module is loaded")
    }
};

window.CartController = CartController;

document.addEventListener('DOMContentLoaded', () => {
    const cartToggle = document.querySelector('.cart-toggle');
    if (cartToggle) {
        cartToggle.addEventListener('click', () => UIController.toggleCart());
    }
});