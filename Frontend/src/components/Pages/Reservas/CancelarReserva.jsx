import { Cabecalho } from "../../Dumb/Cabecalho";
import { Fundo } from "../../Dumb/Fundo";
import { Input } from "../../Dumb/Input";
import { useState } from "react";
import "../../../controller/style.css";
import {
  cancelarReserva,
  consultarReserva,
} from "../../../controller/requestRest";

export const CancelarReserva = () => {
  const [idReserva, setIdReserva] = useState("");
  const [possivelCancelar, setPossivelCancelar] = useState(false);
  const [reservaExiste, setReservaExiste] = useState("");

  const consultaReserva = async () => {
    try {
      const reserva = await consultarReserva(idReserva);
      setReservaExiste(reserva);
      setPossivelCancelar(true);
    } catch (error) {
      setPossivelCancelar(false);
      setReservaExiste(undefined);
    }
  };

  const cancelaReserva = async () => {
    const confirmar = window.confirm(
      "Tem certeza que deseja cancelar a reserva?"
    );

    if (!confirmar) {
      return;
    }

    try {
      const reserva = await cancelarReserva(idReserva);
      if (reserva) {
        alert(`Reserva: ${reservaExiste.idReserva} cancelada com sucesso!`);
      }
    } catch (error) {
      alert("Erro ao cancelar a reserva.");
    }
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Criar Reserva" />
        <div className="flex flex-col gap-5">
          <Input
            value={idReserva}
            setValue={setIdReserva}
            descricao="Digite o id da reserva!"
          />
        </div>
        <button className="mb-30 mt-5" onClick={consultaReserva}>
          Consultar id Reserva
        </button>
        {reservaExiste && (
          <div>{`Para a reserva: ${reservaExiste.idReserva} foram compradas ${reservaExiste.quantidadeCabines} cabines`}</div>
        )}
        <button
          className="mb-30 mt-5"
          onClick={cancelaReserva}
          disabled={!possivelCancelar}
        >
          Cancelar reserva
        </button>
      </div>
      <Fundo />
    </div>
  );
};
