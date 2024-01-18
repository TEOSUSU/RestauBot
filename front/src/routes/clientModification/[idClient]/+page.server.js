export const load = async (loadEvent) => {
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');
    
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
    const id = params.idClient;
    const urlAPI = 'http://localhost:8080';

    const reponseCustomersById = await fetch(urlAPI+`/api/customers/${id}`, {
		method: 'GET',
		headers: headersList
	});

    const customerData = await reponseCustomersById.json();

    const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

    return {
        customerData,
        userInfo}
}