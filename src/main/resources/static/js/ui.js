console.log("HELLO FROM UI module");

export const UIController = {
   elements: {
       cart: document.querySelector('.cart'),
       cartBackground: document.querySelector('.cart-bckg'),
       cartBadge: document.querySelector('.cart-badge'),
       totalPrice: document.getElementById('totalPrice')
   },
   toggleCart() {
       console.log("cart was toggled");
       const {cart, cartBackground} = this.elements;
       const isHidden = cart.style.display === 'none' || !cart.style.display;
       cart.style.display = isHidden ? 'grid' : 'none';
       cartBackground.style.display = isHidden ? 'block' : 'none';
   },
   updateCartUI(data, itemId) {
       try {
            console.log("data and itemId on Cart UI update: ", data, itemId);

            const itemElement = document.getElementById(`item-${itemId}`);
            const countElement = itemElement.querySelector('.counter p');
            const priceElement = itemElement.querySelector('.details p > span');

            const item = data.cartItems.find(item => item.id === itemId);
            countElement.textContent = item.quantity;
            priceElement.textContent = `$${(item.product.price * item.quantity).toFixed(2)}`;

            this.elements.totalPrice.textContent = data.totalPrice.toFixed(2);
            this.elements.cartBadge.textContent = data.cartItems.length;
        } catch (error) {
            console.error('Cart UI update failed:', error);
            throw error;
        }
   },

   testFun(){
        console.log("UI module is loaded")
   }
};

window.UIController = UIController;