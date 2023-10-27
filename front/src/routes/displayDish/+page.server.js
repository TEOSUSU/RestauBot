export const load = async () => {
    const responseAllCategories = await fetch('http://localhost:8080/api/categories/', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allCategories = await responseAllCategories.json();

    const responseAllDishes = await fetch('http://localhost:8080/api/dishes', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allDishes = await responseAllDishes.json();

    return {
        allCategories,
        allDishes
    };
};
