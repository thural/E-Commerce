import { APIController } from './api.js';

const UIController = {
   elements: {
       cart: document.querySelector('.cart'),
       cartBackground: document.querySelector('.cart-bckg'),
       cartBadge: document.querySelector('.cart-badge'),
       cartTotal: document.getElementById('cartTotal')
   },

   toggleCart() {
       const {cart, cartBackground} = this.elements;
       const isHidden = cart.style.display === 'none' || !cart.style.display;
       cart.style.display = isHidden ? 'grid' : 'none';
       cartBackground.style.display = isHidden ? 'block' : 'none';
   },

   updateCartUI(data, itemId) {
       const itemElement = document.getElementById(`item-${itemId}`);
       const countElement = itemElement.querySelector('.counter p');
       const priceElement = itemElement.querySelector('.details p');
       
       countElement.textContent = data.count;
       priceElement.textContent = `$${(data.price * data.count).toFixed(2)}`;
       this.elements.cartTotal.textContent = data.cartTotal.toFixed(2);
       this.elements.cartBadge.textContent = data.totalItems;
   }
};


const CartController = {
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


document.addEventListener('DOMContentLoaded', () => {
   const cartToggle = document.querySelector('.cart-toggle');
   if (cartToggle) {
       cartToggle.addEventListener('click', () => UIController.toggleCart());
   }
});