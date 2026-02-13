import { AppRouter } from "./router/AppRouter";
import { AuthProvider } from "./context/AuthContext";
import { CarritoProvider } from "./context/CarritoContext";
import { Navbar } from "./components/Navbar";

function App() {
  return (
    <AuthProvider>
      <CarritoProvider>
        <Navbar />
        <AppRouter />
      </CarritoProvider>
    </AuthProvider>
  );
}

export default App;
