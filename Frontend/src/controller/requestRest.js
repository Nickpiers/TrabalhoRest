import { v4 } from "uuid";
import { paths } from "./paths";

export const consultarItinerario = async (body) => {
  return await requestBack("/reservas/itinerarios", body);
};

export const criarReserva = async (reserva, navigate) => {
  const clientId = v4();
  const idReserva = generateJavaLikeId();

  const body = { reserva: reserva, clientId, idReserva };

  escutarLink(clientId, navigate);
  try {
    await requestBack("/reservas/criarReserva", body);
  } catch (error) {
    alert("Não foi possível fazer a reserva! Reveja seus dados!");
  }
};

export const cancelarReserva = async () => {
  console.warn(
    await requestBack("/reservas/cancelarReserva", "cancela reserva")
  );
};

export const inscreverPromocao = async (promocao) => {
  const clientId = v4();
  const body = { promocao, clientId };

  escutarPromocao(clientId, promocao);
  console.warn(await requestBack("/reservas/inscreverPromocao", promocao));
};

export const cancelarPromocao = async (promocao) => {
  console.warn(await requestBack("/reservas/cancelarPromocao", promocao));
};

export const requestBack = async (uri, body) => {
  try {
    const options = {
      method: body ? "POST" : "GET",
      headers: {
        "Content-Type": "application/json",
      },
    };

    if (body) {
      options.body = JSON.stringify(body);
    }

    const response = await fetch(`http://localhost:8080${uri}`, options);

    if (!response.ok) {
      throw new Error(`Erro HTTP: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (err) {
    console.error(err);
    throw err;
  }
};

export const escutarLink = (clientId, navigate) => {
  const eventSource = new EventSource(
    `http://localhost:8080/reserva/stream/${clientId}`
  );

  eventSource.onmessage = (event) => {
    navigate(paths.home);
    console.log("🎉 Link recebido:", event.data);
    alert("Link para pagamento: " + event.data);
    eventSource.close();
  };

  eventSource.onerror = (err) => {
    console.error("Erro SSE:", err);
    eventSource.close();
  };
};

export const escutarPromocao = (clientId, idPromocao) => {
  const eventSource = new EventSource(
    `http://localhost:8080/marketing/stream/${idPromocao}/${clientId}`
  );

  eventSource.onmessage = (event) => {
    alert(event.data);
  };

  eventSource.onerror = (err) => {
    eventSource.close();
  };
};

function generateJavaLikeId() {
  const uuid = v4();
  const mostSigBitsHex = uuid.replace(/-/g, "").substring(0, 16);
  let id = BigInt("0x" + mostSigBitsHex);

  const longMax = BigInt("0x7FFFFFFFFFFFFFFF");
  id = id & longMax;

  return id.toString();
}
