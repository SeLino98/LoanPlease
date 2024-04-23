import { LoginStore } from "../Store";
import loginbutton from "./assets/loginbutton.png";
import background from "./assets/splash.png";

const mainarea = {
  display: "flex",
  flexDirection: "column",
  justifyContent: "space-around",
  alignItems: "center",
  width: "80%",
  height: "80%",
  backgroundColor: "rgba(255, 255, 255, 0.64)",
};

const mainstyle = {
  zIndex: "-1",
  position: "absolute",
  width: "100%",
  height: "100vh",
  filter: "blur(1.8px)",
};

const titlestyle = {
  fontFamily: "비트비트체v2",
  fontSize: "80px",
  color: "#186F65",
};

function Login() {
  const setIsLogin = LoginStore((state) => state.setIsLogin);

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        width: "100%",
        height: "100vh",
      }}
    >
      {/* <div style={mainstyle}></div> */}
      <img style={mainstyle} src={background} />
      <div style={mainarea}>
        <div style={titlestyle}>론플리즈</div>
        <img width={200} src="/loanplease.png" />
        <img
          onClick={() => {
            setIsLogin(true);
          }}
          width={300}
          style={{ cursor: "pointer" }}
          src={loginbutton}
        />
      </div>
    </div>
  );
}

export default Login;
