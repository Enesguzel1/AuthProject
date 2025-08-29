import {Link, useNavigate} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useContext, useRef, useState} from "react";
import {AppContext} from "../context/AppContext.jsx";

const EmailVerify = () => {
    const inputRef = useRef([]);
    const [loading, setLoading] = useState(false);
    const {getUserData,userData,isLoggedIn} = useContext(AppContext);
    const navigate = useNavigate();

    const handleChange = (e,index) =>{
        const value = e.target.value.replace(/\D/,"");
        e.target.value = value;
        if(value && index<5){
            inputRef.current[index+1].focus();
        }
    }

    const handleKeyDown = (e,index) =>{
        if(e.key === "Backspace" && !e.target.value && index>0){
            inputRef.current[index-1].focus();
        }
    }

    const handlePaste = (e) =>{
        e.preventDefault();
        const paste = e.clipboardData.getData("text").slice(0,6).split("");
        paste.forEach((digit, index) =>{
            if (inputRef.current[index]){
                inputRef.current[index].value = digit;
            }
        })
        const next = paste.length<6 ? paste.length: 5;
        inputRef.current[next].focus();
    }

    return (
        <div className="email-verify-container d-flex align-items-center justify-content-center vh-100 position-relative"
        style={{background:"linear-gradient(90deg,#6a5af9, #8268f9)",borderRadius:"none"}}>

            <Link to={"/"} className="position-absolute top-0 start-0 p-4 d-flex align-items-center  gap-2 text-decoration-none">
                <img src={assets.logo} alt="logo" height={32} width={32}/>
                <span className="fs-4 fw-semibold text-white">SafeLock</span>
            </Link>

            <div className="p-5 rounded-4 shadow" style={{width:"400px"}}>
                <h4 className="text-white text-center fw-bold mb-2">Email Doğrulama Kodu</h4>
                <p className="text-center text-white text-muted mb-4">
                    Emailinize Gelen Doğrulama Kodunu Giriniz
                </p>

                <div className="d-flex justify-content-between gap-2 mb-4 text-center text-white-50">
                    {[...Array(6)].map((_, index) => (
                        <input
                            key={index}
                            type="text"
                            maxLength={1}
                            className="form-control text-center fs-4 otp-input"
                            ref={(el) => inputRef.current[index] = el}
                            onChange={(e) => handleChange(e,index)}
                            onKeyDown={(e) =>handleKeyDown(e,index)}
                            onPaste={handlePaste}
                        />
                    ))}
                </div>
                <button className="btn btn-warning w-100 fw-semibold" disabled={loading}>
                    {loading ? "Doğrulanıyor..." : "Doğrula"}
                </button>
            </div>

        </div>
    )
}
export default EmailVerify;