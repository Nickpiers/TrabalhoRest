import fundo from "../public/fundoTela.jpg";

export const Home = () => {
  return (
    <div className="flex min-h-screen">
      <div className="w-1/2 flex flex-col justify-center items-center p-10 relative">
        <h1 className="absolute top-4 left-0 w-full text-center text-2xl font-bold">
          Reserva de Cruzeiros
        </h1>
        <div className="flex flex-col gap-5 mt-20">
          <button>Consultar Itinerários</button>
          <button>Efetuar Reserva</button>
          <button>Cancelar Reserva</button>
          <button>Inscreva-se na promoção!</button>
          <button>Cancelar promoção</button>
        </div>
      </div>
      <div
        className="w-1/2 bg-cover bg-center"
        style={{ backgroundImage: `url(${fundo})` }}
      />
    </div>
  );
};
