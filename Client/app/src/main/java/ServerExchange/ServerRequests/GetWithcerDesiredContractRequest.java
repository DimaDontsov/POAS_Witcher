package ServerExchange.ServerRequests;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import ServerExchange.ProfilePart;

/**
 * Created by Дима on 20.02.2018.
 */

public class GetWithcerDesiredContractRequest extends TokenServerRequest<ArrayList<ProfilePart>> {

    @Override
    protected RequestType getRequestType(){ return RequestType.GET; }

    private String GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME = "GetWithcerDesiredContract";

    private long id_contract;

    public GetWithcerDesiredContractRequest(String serverAddress) {
        super(serverAddress);
    }

    @Override
    protected ServerMethod getMethod() {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id",id_contract);

        return new ServerMethod(GET_WITCHER_DESIRED_CONTRACT_METHOD_NAME, params);
    }

    class JsonGetWithcerDesiredContractServerAnswer extends JsonServerAnswer{
        public class JsonObj {
            public class JsonWitchers {
                public long count;

                public class JsonWitcherInfo {
                    public long id;
                    public String name;
                    ProfilePart convert(){
                        return new ProfilePart(id,name);
                    }
                }

                public HashMap<String, JsonWitcherInfo> witcher;
            }

            public JsonWitchers witchers;
        }
        public JsonObj object;

        @Override
        public ArrayList<ProfilePart> convert() {
            LinkedList<ProfilePart> prfls = new LinkedList<>();
            for (Map.Entry<String, JsonObj.JsonWitchers.JsonWitcherInfo> winfo : object.witchers.witcher.entrySet()){
                prfls.addLast(winfo.getValue().convert());
            }
            return new ArrayList<>(prfls);
        }
    }

    @Override
    protected Class<? extends JsonServerAnswer> getJsonAnswerClass() {

        return JsonGetWithcerDesiredContractServerAnswer.class;
    }

    public void addCommentContract(long id_contract, IServerAnswerHandler onGetWithcerDesiredContractHandler) throws IOException {
        this.id_contract = id_contract;
        startRequest(onGetWithcerDesiredContractHandler);
    }
}
