import {Link, useNavigate} from "react-router-dom";
import {assets} from "../assets/assets.js";
import {useState} from "react";

const Login = () => {
    const [isCreateAccount, setIsCreateAccount] = useState(false);
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
                <form>
                    {
                        isCreateAccount && (
                            <div className="mb-3">
                                <label htmlFor="fullname" className="form-label">İsim - Soyisim</label>
                                <input
                                    type="text"
                                    id="fullname"
                                    className="form-control"
                                    placeholder="John Doe" required/>
                            </div>
                        )
                    }
                    <div className="mb-3">
                        <label htmlFor="email" className="form-label">Email</label>
                        <input
                            type="email"
                            id="email"
                            className="form-control"
                            placeholder="E-mail" required/>
                    </div>
                    <div className="mb-3">
                        <label htmlFor="password" className="form-label">Şifre</label>
                        <input
                            type="password"
                            id="password"
                            className="form-control"
                            placeholder="*********" required/>
                    </div>
                    {!isCreateAccount && (
                        <div className="d-flex justify-content-between mb-3">
                            <Link to="/reset-password" className="text-decoration-none">
                                Şifremi Unuttum
                            </Link>
                        </div>
                    )}

                    <button type="submit" className="btn btn-primary w-100">
                        {isCreateAccount ? ("Kayıt Ol"): "Giriş Yap"}
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