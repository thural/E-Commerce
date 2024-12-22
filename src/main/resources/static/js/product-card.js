document.addEventListener('DOMContentLoaded', function() {
    // Add click event listeners to all add-to-cart buttons
    document.querySelectorAll('.add-to-cart-btn').forEach(button => {
        button.addEventListener('click', function() {
            const productData = {
                id: this.getAttribute('data-product-id'),
                name: this.getAttribute('data-product-name'),
                price: this.getAttribute('data-product-price'),
                image: this.getAttribute('data-product-image')
            };

            addToCart(productData);
        });
    });
});

function addToCart(product) {
    // Send POST request to add item to cart
    fetch('/api/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // Add CSRF token if required by Spring Security
            // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        },
        body: JSON.stringify({
            id: product.id,
            type: 'increment',
            item: product
        })
    })
    .then(response => response.json())
    .then(data => {
        // Handle successful addition to cart
        console.log('Product added to cart:', data);
        // You could trigger a cart update event or show a notification here
    })
    .catch(error => {
        console.error('Error adding product to cart:', error);
    });
}