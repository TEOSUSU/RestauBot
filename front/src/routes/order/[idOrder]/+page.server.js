export const load = async (loadEvent) => {
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');
    
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
	const idOrder = params.params.idOrder;

	const reponseOrder = await fetch(`http://localhost:8080/api/purchases/${idOrder}`, {
		method: 'GET',
		headers: headersList
	});
	const order = await reponseOrder.json();

	const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

	return {
		order,
		userInfo
	};
};

