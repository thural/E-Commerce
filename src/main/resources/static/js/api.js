export const APIController = {
    async updateCart(itemId, action) {
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        try {
            const response = await fetch('http://localhost:8080/api/v1/cart/handleAction', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify({ itemId, action })
            });
            return await response.json();
        } catch (error) {
            console.error('Cart update failed:', error);
            throw error;
        }
    }
};