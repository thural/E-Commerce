export const UIController = {
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

window.UIController = UIController;