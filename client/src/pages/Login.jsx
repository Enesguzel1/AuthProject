import {Link, useNavigate} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useContext, useState} from "react";
import axios from "axios";
import {toast} from "react-toastify";
import {AppConstants} from "../util/constants.js";
import {AppContext} from "../context/AppContext.jsx";

const Login = () => {

    const [isCreateAccount, setIsCreateAccount] = useState(false);
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setLoading] = useState(false);
    const {BASE_URL,setIsLoggedIn,getUserData} = useContext(AppContext);
    const navigate = useNavigate();

    const onSubmitHandler = async (e) => {
        e.preventDefault();
        axios.defaults.withCredentials = true;
        setLoading(true);
        try{
            if (isCreateAccount){
                    //kayıt işlemi
                const response = await axios.post(`${BASE_URL}/register`, {
                    name,
                    email,
                    password,
                })
                if(response.status === 201){
                    setIsLoggedIn(true);
                    await getUserData();
                    navigate("/");
                    toast.success("Kayıt başarılı");
                }else{
                    console.log("error");
                    toast.error("Bu email zaten kullanılıyor");
                }
            }else{
                //login işlemi
                const response = await axios.post(`${BASE_URL}/login`, {
                    email,
                    password,
                })
                if(response.status === 200){
                    setIsLoggedIn(true)
                    await getUserData()
                    navigate("/");
                    toast.success("Giriş başarılı");
                }else{
                    toast.error("Şifre/Kullanıcı adı yanlış!")
                }
            }
        }catch(err){
            console.log(err);
            toast.error(err.response.data.message);
        }finally{
            setLoading(false);
        }
    }

    return (
        <div className="position-relative min-vh-100 d-flex justify-content-center align-items-center"
        style={{background:"linear-gradient(90deg,#6a5af9,#8268f9)",border:"none"}}>

            <div style={{position:"absolute",top:"20px",left:"30px",display:"flex",alignItems:"center"}}>
                <Link to="/" style={{display:"flex",gap:5,alignItems:"center",fontWeight:"bold",fontSize:"24px",textDecoration:"none"}}>
                    <img src={assets.logo} alt="logo" height={32} width={32} />
                    <span className="fw-bold fs-4 text-light"> SafeLock</span>
                </Link>
            </div>
            <div className="card p-4" style={{maxWidth:"400px",width:"100%"}}>
                <h2 className="text-center mb-4">
                    {isCreateAccount ? ("Kayıt Ol") : "Giriş Yap"}
                </h2>
                <form onSubmit={onSubmitHandler}>
                    {
                        isCreateAccount && (
                            <div className="mb-3">
                                <label htmlFor="fullname" className="form-label">İsim - Soyisim</label>
                                <input
                                    type="text"
                                    id="fullname"
                                    className="form-control"
                                    placeholder="John Doe" required
                                    onChange={(e) => setName(e.target.value)}
                                    value={name}
                                />
                            </div>
                        )
                    }
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="form-control"
                            placeholder="E-mail" required
                            onChange={(e) => setEmail(e.target.value)}
                            value={email}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Şifre</label>
                        <input
                            type="password"
                            id="password"
                            className="form-control"
                            placeholder="*********" required
                            onChange={(e) => setPassword(e.target.value)}
                            value={password}
                        />
                    </div>
                    {!isCreateAccount && (
                        <div className="d-flex justify-content-between mb-3">
                            <Link to="/reset-password" className="text-decoration-none">
                                Şifremi Unuttum
                            </Link>
                        </div>
                    )}

                    <button type="submit" className="btn btn-primary w-100" disabled={loading}>
                        {loading ? "Yükleniyor..." : isCreateAccount ? ("Kayıt Ol"): "Giriş Yap"}
                    </button>
                </form>
                <div className="text-center mt-3">
                    <p className="mb-0">
                        {isCreateAccount ?
                            (<>
                                Hesabınız var mı ?{" "}
                                <span onClick={() => setIsCreateAccount(false)}
                                    className="text-decoration-underline" style={{cursor:"pointer"}}>
                                    Giriş Yap
                                </span>
                            </>
                            ): (
                                <>
                                    Hesabınız yok mu ? {" "}
                                    <span onClick={() => setIsCreateAccount(true)}
                                        className="text-decoration-underline" style={{cursor:"pointer"}}>
                                     Kayıt Ol
                                </span>
                                </>
                            )
                        }
                    </p>
                </div>
            </div>

        </div>
    )
}
export default Login;