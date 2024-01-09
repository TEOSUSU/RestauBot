export const load = async (loadEvent) => {
	const { fetch, cookies, params } = loadEvent;
	const token = cookies.get('token');
    
	const headersList = {
		'Content-Type': 'application/json;charset=UTF-8',
		Authorization: `Bearer ${token}`
	};
    const id = params.id;
    const urlAPI = 'http://localhost:8080';

    const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
		method: 'GET',
		headers: headersList
	});
	const userInfo = await responseUserInfo.json();

    const reponseRestaurantById = await fetch(urlAPI+`/api/restaurant/id/${id}`, {
		method: 'GET',
		headers: headersList
	});

    const restaurantById = await reponseRestaurantById.json();

    return {
        restaurantById,
        userInfo}
}


