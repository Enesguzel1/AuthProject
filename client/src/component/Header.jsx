import {assets} from "../assets/assets.js";

const Header = () => {
    return (
        <div className="text-center d-flex flex-column align-items-center justify-content-center py-5 px-3" style={{minHeight:"80vh"}}>
            <img src={assets.auth_home} alt="header" width={120} className="mb-4"/>

            <h5 className="fw-semibold">
                Merhaba Geliştirici <span role="img" aria-label="wave">👋</span>
            </h5>
            <h1 className="fw-bold display-5 mb-3">Ürünüme Hoşgeldin!</h1>

            <p className="text-muted fs-5 mb-4" style={{maxWidth:"500px"}}>
                Kısa bir tur ile nasıl çalıştığını görelim!
            </p>

            <button className="btn btn-outline-dark rounded-pill pc-4 py-2">
                Başlayalım
            </button>
        </div>
    )
}
export default Header;