export const load = async (loadEvent) => {
    const { fetch, params } = loadEvent;
    const id = params.id;
    const urlAPI = 'http://localhost:8080';

    const reponseRestaurantById = await fetch(urlAPI+`/api/restaurant/id/${id}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});

    const restaurantById = await reponseRestaurantById.json();

    return {restaurantById}
}


