export const load = async (loadEvent) => {
	const { fetch, cookies } = loadEvent;
	const token = cookies.get('token');

	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};

    const responseProduct = await fetch('http://localhost:8080/api/dishes/${productId}', {
        method: 'GET',
		headers: headersList
    });
    const product = await responseProduct.json();

    const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

    return {
        product,
        userInfo
    };
};