export const load = async (restaurantId) => {
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
    
    const responseAllDishes = await fetch('http://localhost:8080/api/dishes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allDishes = await responseAllDishes.json();

    return {
        allCategories,
        allTypes,
        allDishes
    };
};
