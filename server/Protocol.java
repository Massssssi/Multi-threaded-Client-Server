public class Protocol {
    private int listNum;
    private String memberName = null;


    public String processInput(String theInput){
        String[] input = theInput.split(" ");


        switch (input[0]) {
            //case where a client wants to see information about the lists.
            case "totals":
                if (input.length > 1) {
                    return "error";
                }
                return "totals";

             // case where a client wants to add member to a list.
            case "join":
                if (input.length <= 2) {
                    return "error";
                }
                try {
                    this.listNum = Integer.parseInt(input[1]) - 1;
                } catch (Exception e) {
                    return "error";
                }

                for (int i = 2; i < input.length; i++) {
                    //Parsing the memberName into 1 string
                    if (memberName == null) {
                        memberName = input[i];
                    } else {
                        memberName = memberName + " " + input[i];
                    }
                }
                return "join";

             // case a client wants to see information about a specified list.
            case "list":
                if (input.length > 2) {
                    return "error";
                }
                try {
                    this.listNum = Integer.parseInt(input[1]) - 1;
                } catch (Exception e) {
                    return "error";
                }
                return "list";
        }

        return "error";
    }
    public int getListNum() {
        return listNum;
    }

    public String getMemberName() {
        return memberName;
    }
    }
