import './App.css'
import {ToastContainer} from "react-toastify";
import {Route, Routes} from "react-router-dom";
import Home from "./pages/Home.jsx";
import Login from "./pages/Login.jsx";
import EmailVerify from "./pages/EmailVerify.jsx";
import ResetPassword from "./pages/ResetPassword.jsx";
import Menubar from "./component/Menubar.jsx";

function App() {
  return (
      <div>
          <ToastContainer/>
          <Routes>
              <Route path="/login" element={<Login/>} />
              <Route path="/email-verify" element={<EmailVerify/>} />
              <Route path="/reset-password" element={<ResetPassword/>} />
              <Route path="/" element={<Home/>} />
          </Routes>
      </div>
  )
}

export default App
