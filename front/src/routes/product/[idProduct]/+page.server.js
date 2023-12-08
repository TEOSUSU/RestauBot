export const load = async (params) => {
    const idProduct = params.params.idProduct;

    const responseProduct = await fetch(`http://localhost:8080/api/dishes/${idProduct}`, {
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
