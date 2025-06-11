import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Home } from "./components/Pages/Home";
import { ConsultarItinerarios } from "./components/Pages/ConsultarItinerarios";
import { Promocoes } from "./components/Pages/Promocoes";
import { Reservas } from "./components/Pages/Reservas/Reservas";
import { MarcarReserva } from "./components/Pages/Reservas/MarcarReserva";
import { CancelarReserva } from "./components/Pages/Reservas/CancelarReserva";

export const App = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route
          path="/consultar-itinerarios"
          element={<ConsultarItinerarios />}
        />
        <Route path="/reservas" element={<Reservas />} />
        <Route path="/promocoes" element={<Promocoes />} />
        <Route path="/marcar-reserva" element={<MarcarReserva />} />
        <Route path="/cancelar-reserva" element={<CancelarReserva />} />
      </Routes>
    </BrowserRouter>
  );
};
