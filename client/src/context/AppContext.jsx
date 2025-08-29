import {createContext, useState} from "react";
import {AppConstants} from "../util/constants.js";
import axios from "axios";
import {toast} from "react-toastify";

export const AppContext = createContext()

export const AppContextProvider = (props) => {

    const BASE_URL = AppConstants.BASE_URL;
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userData, setUserData] = useState(false);

    const getUserData = async () => {
        try{
            const response = await axios.get(`${BASE_URL}/profile`);
            if(response.status === 200){
                setUserData(response.data);
            }else {
                toast.error("Kullanıcı bilgileri çekilemedi");
            }
        }catch(err){
            console.log(err.message);
        }
    }

    const contextValue = {
        BASE_URL,
        isLoggedIn,
        setIsLoggedIn,
        userData,
        setUserData,
        getUserData,
    }

    return (
        <AppContext.Provider value={contextValue}>
            {props.children}
        </AppContext.Provider>
    )

}