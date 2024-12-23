import { APIController } from './api.js';

console.log("HELLO FROM PRODUCT CARD");

document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.add-to-cart-btn').forEach(button => {
        button.addEventListener('click', function() {
        console.log("add cart button was clicked");
            const productData = {
                id: this.getAttribute('data-product-id'),
                name: this.getAttribute('data-product-name'),
                price: this.getAttribute('data-product-price'),
                image: this.getAttribute('data-product-image')
            };
              APIController.updateCart(productData.id, 'INCREMENT');
        });
    });
});