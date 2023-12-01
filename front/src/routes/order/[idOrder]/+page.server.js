export const load = async (params) => {
	const idOrder = params.params.idOrder;

	const reponseOrder = await fetch(`http://localhost:8080/api/purchases/${idOrder}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const order = await reponseOrder.json();

	return {
		order
	};
};

