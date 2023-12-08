export const load = async (params) => {
	const idRestaurant = params.params.idRestaurant;

    const responseDishes = await fetch(`http://localhost:8080/api/dishes/restaurant/${idRestaurant}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allDishes = await responseDishes.json();

    
    const responseRestaurant = await fetch(`http://localhost:8080/api/restaurant/id/${idRestaurant}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const restaurant = await responseRestaurant.json();

    const responseAllCategories = await fetch('http://localhost:8080/api/categories/', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allCategories = await responseAllCategories.json();

    const reponseAllTypes = await fetch('http://localhost:8080/api/types/', {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allTypes = await reponseAllTypes.json();

    const reponseAllMenus = await fetch(`http://localhost:8080/api/menus/restaurant/${idRestaurant}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allMenus = await reponseAllMenus.json();

    return {
        allCategories,
        allTypes,
        allDishes,
        allMenus,
        restaurant
    };
};
