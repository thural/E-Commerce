console.log("HELLO FROM UI module");
export const UIController = {
    elements: {
        cart: document.querySelector('.cart'),
        cartBackground: document.querySelector('.cart-bckg'),
        cartBadge: document.querySelector('.cart-badge'),
        totalPrice: document.getElementById('totalPrice'),
        itemsContainer: document.querySelector('.cart .items')
    },

    toggleCart() {
        console.log("cart was toggled");
        const {cart, cartBackground} = this.elements;
        const isHidden = cart.style.display === 'none' || !cart.style.display;
        cart.style.display = isHidden ? 'grid' : 'none';
        cartBackground.style.display = isHidden ? 'block' : 'none';
    },

    updateCartUI(data, productId) {
        try {
            console.log("data and productId on Cart UI update: ", data, productId);

            // Update total price and cart badge
            this.elements.totalPrice.textContent = data.totalPrice.toFixed(2);
            this.elements.cartBadge.textContent = data.cartItems.length;

            // Find cart item by product ID
            const item = data.cartItems.find(item => item.product.id === Number(productId));
            if (!item) {
                console.warn(`Item with product ID ${productId} not found in cart`);
                return;
            }

            const itemElement = document.getElementById(`item-${productId}`);
            if (!itemElement) {
                // If element doesn't exist, we'll need a page refresh to get the updated cart HTML
                window.location.reload();
                return;
            }

            // Update existing item
            const countElement = itemElement.querySelector('.counter p');
            const priceElement = itemElement.querySelector('.details p > span');

            countElement.textContent = item.quantity;
            priceElement.textContent = (item.product.price * item.quantity).toFixed(2);

            // Remove item if quantity is 0
            if (item.quantity <= 0) {
                itemElement.remove();
            }

        } catch (error) {
            console.error('Cart UI update failed:', error);
            throw error;
        }
    },

    testFun() {
        console.log("UI module is loaded");
    }
};

window.UIController = UIController;