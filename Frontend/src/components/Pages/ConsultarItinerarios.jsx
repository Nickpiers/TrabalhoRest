import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Cabecalho } from "../Dumb/Cabecalho";
import { Input } from "../Dumb/Input";
import { consultarItinerario } from "../../controller/requestRest";
import { Fundo } from "../Dumb/Fundo";
import { CruzeiroCard } from "../Dumb/CruzeiroCard";

export const ConsultarItinerarios = () => {
  const [destino, setDestino] = useState("");
  const [dataEmbarque, setDataEmbarque] = useState("");
  const [portoEmbarque, setPortoEmbarque] = useState("");
  const [listaCruzeiros, setListaCruzeiros] = useState([]);

  const buscaItinerarios = async () => {
    const lista = await consultarItinerario({
      destino,
      dataEmbarque,
      portoEmbarque,
    });
    setListaCruzeiros(lista);
  };

  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <Cabecalho titulo="Consultar Itinerários" />
        <div className="flex flex-col gap-5">
          <Input value={destino} setValue={setDestino} descricao="Destino" />
          <Input
            value={dataEmbarque}
            setValue={setDataEmbarque}
            descricao="Data de Embarque"
          />
          <Input
            value={portoEmbarque}
            setValue={setPortoEmbarque}
            descricao="Porto de Embarque"
          />
        </div>
        <button className="mb-30 mt-5" onClick={buscaItinerarios}>
          Consultar
        </button>
        <div className="flex flex-col gap-5">
          {listaCruzeiros.length > 0 &&
            listaCruzeiros.map((cruzeiro) => (
              <CruzeiroCard cruzeiro={cruzeiro} veioDoConsultarItinerario />
            ))}
        </div>
      </div>
      <Fundo />
    </div>
  );
};
