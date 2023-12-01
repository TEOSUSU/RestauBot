export const load = async (params) => {
	const idProduct = params.params.idProduct;

	const reponseAllCategories = await fetch('http://localhost:8080/api/categories/', {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allCategories = await reponseAllCategories.json();

	const reponseAllTypes = await fetch('http://localhost:8080/api/types/', {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allTypes = await reponseAllTypes.json();

	const reponseProductSelected = await fetch(`http://localhost:8080/api/dishes/${idProduct}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const productSelected = await reponseProductSelected.json();

	return {
		allCategories,
		allTypes,
		productSelected
	};
};

