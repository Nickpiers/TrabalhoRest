import { useNavigate } from "react-router-dom";
import { paths } from "../../controller/paths";

export const CruzeiroCard = ({ descricaoCruzeiro, id }) => {
  const navigate = useNavigate();

  return (
    <button
      className="text-lg px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
      onClick={() => navigate(paths.reservas)}
    >
      {descricaoCruzeiro}
    </button>
  );
};
