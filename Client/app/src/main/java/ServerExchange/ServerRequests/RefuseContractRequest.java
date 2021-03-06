package ServerExchange.ServerRequests;


import java.util.HashMap;

import ServerExchange.ServerRequests.ServerAnswerHandlers.IServerAnswerHandler;

/**
 * Created by Dima on 19.02.2018.
 */

public class RefuseContractRequest extends TokenServerRequest<Boolean> {

    @Override
    protected RequestType getRequestType(){ return RequestType.POST; }
	
	private String REFUSE_CONTRACT_METHOD_NAME = "RefuseContract";

    private long id_contract;

    public RefuseContractRequest(String address) {super(address);}
    public RefuseContractRequest() {super();}


    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id_contract", id_contract);

        return new ServerMethod(REFUSE_CONTRACT_METHOD_NAME, params);
    }

    class JsonRefuseContractServerAnswer extends JsonServerAnswer{

        @Override
        public Boolean convert() {
            return isStatusOk();
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {


        return JsonRefuseContractServerAnswer.class;
    }

    public void RefuseContract(long id_contract, IServerAnswerHandler onRefuseContractHandler)  {
        this.id_contract = id_contract;
        startRequest(onRefuseContractHandler);
    }
}
