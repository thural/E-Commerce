import { APIController } from './api.js';
import { UIController } from './ui.js';

console.log("hello from CART JS");

export const CartController = {
   async handleCartAction(itemId, action, currentCount) {
       if (action === 'DECREMENT' && currentCount <= 0) return;
       try {
           const data = await APIController.updateCart(itemId, action);
           UIController.updateCartUI(data, itemId);
       } catch (error) {
           console.error('Cart action failed:', error);
       }
   },
   checkout() {
       window.location.href = '/checkout';
   }
};

window.CartController = CartController;

document.addEventListener('DOMContentLoaded', () => {
   const cartToggle = document.querySelector('.cart-toggle');
   if (cartToggle) {
       cartToggle.addEventListener('click', () => UIController.toggleCart());
   }
});