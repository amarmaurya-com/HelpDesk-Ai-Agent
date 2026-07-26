import axios from "axios";

const baseUrl = "http://localhost:8080/api/v1/ai";

export const sendMessageToServer = async (message, email) => {
    const response = await axios.post(`${baseUrl}`, message, {
        headers: {
            email: email,
        }
    })
    return response.data;
}