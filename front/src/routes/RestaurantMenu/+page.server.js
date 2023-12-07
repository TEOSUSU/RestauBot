export const load = async () => {
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

    const reponseAllMenus = await fetch(`http://localhost:8080/api/menus`, {
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
        allMenus
    };
};
