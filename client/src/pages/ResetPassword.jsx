import {Link, useNavigate} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useContext, useRef, useState} from "react";
import {AppContext} from "../context/AppContext.jsx";
import axios from "axios";
import {toast} from "react-toastify";

const ResetPassword = () => {
    const inputRef = useRef([]);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [email, setEmail] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [isEmailSent, setIsEmailSent] = useState(false);
    const [otp, setOtp] = useState("");
    const [isOtpSubmitted, setIsOtpSubmitted] = useState(false);
    const {getUserData,isLoggedIn,userData,BASE_URL} = useContext(AppContext);

    axios.defaults.withCredentials=true;

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

    const handleVerify = async () => {
        const otp = inputRef.current.map(input=>input.value).join("");
        if(otp.length!==6){
            toast.error("6 haneyi doldurunuz");
            return;
        }
        setLoading(true);
        try{
            const response = await axios.post(BASE_URL+"/reset-password",{otp});
            if(response.status === 200){
                toast.success("Şifre Değiştirme Başarılı!");
                getUserData();
                navigate("/");
            }else {
                toast.error("Geçersiz Kod");
            }
        }catch(e){
            console.log(e);
            toast.error("Şifre değiştirilemedi. Daha sonra tekrar deneyin.");
        }finally{
            setLoading(false);
        }

    }


    return (
        <div className="email-verify-container d-flex align-items-center justify-content-center vh-100 position-relative"
             style={{background:"linear-gradient(90deg,#6a5af9, #8268f9)",borderRadius:"none"}}>

            <Link to={"/"} className="position-absolute top-0 start-0 p-4 d-flex align-items-center  gap-2 text-decoration-none">
                <img src={assets.logo} alt="logo" height={32} width={32}/>
                <span className="fs-4 fw-semibold text-white">SafeLock</span>
            </Link>

            {!isEmailSent && (
                <div className="rounded-4 p-5 text-center bg-white">
                    <h4 className="mb-2">Şifre Sıfırlama</h4>
                    <p className="mb-4">
                        Email
                    </p>
                    <form>
                        <div className="input-group mb-4 bg-secondary bg-opacity-10 rounded-pill">
                            <span className="input-group-text  bg-transparent border-0 ps-4">
                                <i className="bi bi-envelope"></i>
                            </span>
                            <input type="email"
                                   className="form-control bg-transparent border-0  ps-1 pe-4 rounded-end"
                                   placeholder="E-mail"
                                   style={{height:"50px"}}
                                   onChange={(e) => setEmail(e.target.value)}
                                   value={email}
                                   required
                            />
                        </div>
                        <button className="btn btn-primary w-100 py-2" type="submit">Devam et</button>
                    </form>
                </div>
            )}

            {!isOtpSubmitted && isEmailSent && (
                <div className="p-5 rounded-4 shadow" style={{width:"400px"}}>
                    <h4 className="text-white text-center fw-bold mb-2">Şifre Değiştirme Kodu</h4>
                    <p className="text-center text-white text-muted mb-4">
                        Emailinize Gelen Kodu Giriniz
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
            )}


            {isOtpSubmitted && isEmailSent && (
                <div className="rounded-4 p-4 text-center bg-white" style={{width:"100%",maxWidth:"400px"}}>
                    <h4>Yeni Şifre</h4>
                    <p className="mb-4">Yeni şifrenizi giriniz.</p>
                    <form>
                        <div className="input-group mb-4 bg-secondary bg-opacity-10 rounded-pill">
                                        <span className="input-group-text  bg-transparent border-0 ps-4">
                                            <i className="bi bi-person-fill-lock"></i>
                                        </span>
                            <input type="password"
                                   className="form-control bg-transparent border-0 ps-1 pe-4 rounded-end"
                                   placeholder="********"
                                   style={{height:"50px"}}
                                   onChange={(e) => setNewPassword(e.target.value)}
                                   value={newPassword}
                                   required/>
                        </div>
                        <button type="submit" className="btn btn-primary w-100">
                            Onayla
                        </button>
                    </form>
                </div>
            )}

        </div>
    )
}
export default ResetPassword;