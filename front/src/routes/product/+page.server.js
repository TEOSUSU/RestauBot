export const load = async (productId) => {
    const responseProduct = await fetch('http://localhost:8080/api/dishes/${productId}', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const product = await responseProduct.json();

    return {
        product
    };
};
