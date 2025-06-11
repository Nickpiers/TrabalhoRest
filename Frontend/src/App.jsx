import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Home } from "./components/Pages/Home";
import { ConsultarItinerarios } from "./components/Pages/ConsultarItinerarios";
import { Reservas } from "./components/Pages/Reservas/Reservas";
import { MarcarReserva } from "./components/Pages/Reservas/MarcarReserva";
import { CancelarReserva } from "./components/Pages/Reservas/CancelarReserva";
import { Promocoes } from "./components/Pages/Promocoes/Promocoes";
import { InscreverPromocao } from "./components/Pages/Promocoes/InscreverPromocao";
import { CancelarPromocao } from "./components/Pages/Promocoes/CancelarPromocao";

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
        <Route path="/marcar-reserva" element={<MarcarReserva />} />
        <Route path="/cancelar-reserva" element={<CancelarReserva />} />
        <Route path="/promocoes" element={<Promocoes />} />
        <Route path="/inscrever-promocao" element={<InscreverPromocao />} />
        <Route path="/cancelar-promocao" element={<CancelarPromocao />} />
      </Routes>
    </BrowserRouter>
  );
};
