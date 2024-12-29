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
        const { cart, cartBackground } = this.elements;
        const isHidden = cart.style.display === 'none' || !cart.style.display;
        cart.style.display = isHidden ? 'grid' : 'none';
        cartBackground.style.display = isHidden ? 'block' : 'none';
    },

    createCartItemElement(item) {
        return `
                <div class="item" id="item-${item.product.id}">
                    <div class="image">
                        <img src="/public/images/${item.product.imageFileName}" alt="${item.product.name}">
                    </div>
                    <div class="details">
                        <h5>${item.product.name}</h5>
                        <p class="brand">${item.product.brand || 'Brand'}</p>
                        <p>$<span>${(item.product.price * item.quantity).toFixed(2)}</span></p>
                        <div class="counter">
                            <button onclick="CartController.handleCartAction(${item.product.id},'DECREMENT',${item.quantity})">-</button>
                            <p>${item.quantity}</p>
                            <button onclick="CartController.handleCartAction(${item.product.id},'INCREMENT',${item.quantity})">+</button>
                        </div>
                    </div>
                </div>
            `;
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
                const itemElement = document.getElementById(`item-${productId}`);
                itemElement.remove();
                return;
            }

            let itemElement = document.getElementById(`item-${item.product.id}`);


            // If item doesn't exist in cart, create it
            if (!itemElement) {
                console.warn("itemElement is not found on the cart, appending new element");
                const newItemHtml = this.createCartItemElement(item);
                this.elements.itemsContainer.insertAdjacentHTML('beforeend', newItemHtml);
                itemElement = document.getElementById(`item-${item.product.id}`);
            } else if (item.quantity >= 1) {
                console.log("updating cart item")
                // Update existing item
                const countElement = itemElement.querySelector('.counter p');
                const priceElement = itemElement.querySelector('.details p > span');

                countElement.textContent = item.quantity;
                priceElement.textContent = (item.product.price * item.quantity).toFixed(2);
            } else {
                console.log("removing cart item...")
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