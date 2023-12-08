export const load = async (loadEvent) => {
    const { fetch, params } = loadEvent;
    const id = params.idClient;
    const urlAPI = 'http://localhost:8080';

    const reponseCustomersById = await fetch(urlAPI+`/api/customers/${id}`, {
		method: 'GET',
		headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
	});

    const customerData = await reponseCustomersById.json();

    return {customerData}
}