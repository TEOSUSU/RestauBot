export const load = async (params) => {
	const idMenu = params.params.idMenu;

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

    const reponseAllDishes = await fetch('http://localhost:8080/api/dishes/', {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const allDishes = await reponseAllDishes.json();

	const reponseMenuSelected = await fetch(`http://localhost:8080/api/menus/${idMenu}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});
	const menuSelected = await reponseMenuSelected.json();

	return {
		allCategories,
		allTypes,
        allDishes,
		menuSelected
	};
};

