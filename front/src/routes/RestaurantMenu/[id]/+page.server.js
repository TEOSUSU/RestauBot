export const load = async (loadEvent) => {
    const { fetch, cookies, params } = loadEvent;
    const token = cookies.get('token');

    const headersList = {
        'Content-Type': 'application/json;charset=UTF-8',
        Authorization: `Bearer ${token}`
    };
    const urlAPI = 'http://localhost:8080';
    const idRestaurant = params.id;


    const responseAllCategories = await fetch('http://localhost:8080/api/categories/', {
        method: 'GET',
        headers: headersList
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
        headers: headersList
    });
    const allDishes = await responseAllDishes.json();

    const responseUserInfo = await fetch('http://localhost:8080/auth/getUserInfo', {
        method: 'GET',
        headers: headersList
    });
    const userInfo = await responseUserInfo.json();

    const reponseAllMenus = await fetch(`http://localhost:8080/api/menus`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const allMenus = await reponseAllMenus.json();

    const responseRestaurant = await fetch(urlAPI + `/api/restaurant/id/${idRestaurant}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const restaurant = await responseRestaurant.json();

    const responseAverageNote = await fetch(`http://localhost:8080/api/note/averageNote/${idRestaurant}`, {
        method: 'GET',
        headers: headersList
    });
    const averageNote = await responseAverageNote.json();

    return {
        allCategories,
        allTypes,
        allDishes,
        allMenus,
        userInfo,
        restaurant,
        averageNote
    };
};
