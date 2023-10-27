export const load = async () => {

    const reponseMailCustomer = await fetch('http://localhost:8080/api/customers/mail/', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json;charset=UTF-8'
        }
    });
    const mailCustomer = await reponseMailCustomer.json();

    return mailCustomer;
};