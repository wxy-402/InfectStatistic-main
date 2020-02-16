/**
 * InfectStatistic
 * TODO
 *
 * @author wxy-402
 * @version
 * @since
 */
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class InfectStatistic {

    public String log_path;
    public String out_path;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date(System.currentTimeMillis());
    public String date = formatter.format(d);
    public ArrayList<String> type_list = new ArrayList<>();
    public ArrayList<String> province_list = new ArrayList<>();
    public static String[] province_str = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃",
            "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", "吉林",
            "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海",
            "四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};

    static class province{
        private String name;
        private int ip;//感染
        private int sp;//疑似
        private int cure;//治愈
        private int dead;//死亡
        province(String name, int ip, int sp, int cure, int dead){
            this.name = name;
            this.ip = ip;
            this.sp = sp;
            this.cure = cure;
            this.dead = dead;
        }
        public String getName(){
            return name;
        }
        public int getIp(){
            return ip;
        }
        public int getSp(){
            return sp;
        }
        public int getCure(){
            return cure;
        }
        public int getDead() {
            return dead;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setIp(int ip) {
            this.ip = ip;
        }
        public void setSp(int sp) {
            this.sp = sp;
        }
        public void setCure(int cure) {
            this.cure = cure;
        }
        public void setDead(int dead) {
            this.dead = dead;
        }
        public String printResult(){
            return name+" 感染患者"+ip+"人"+" 疑似患者"+sp+"人"+ " 治愈"+cure+"人"+" 死亡"+dead+"人";
        }
        public String printIp(){
            return " 感染患者"+ip+"人";
        }
        public String printSp(){
            return " 疑似患者"+sp+"人";
        }
        public String printCure(){
            return " 治愈"+cure+"人";
        }
        public String printDead(){
            return " 死亡"+dead+"人";
        }
        public int getPosition() {
            int position = 0;
            for(int i  = 0; i < province_str.length; i++) {
                if(province_str[i].equals(name)) {
                    position = i;
                    break;
                }
            }
            return position;
        }
    }
    /**
     * 解析命令行参数
     */
    class CmdArgs{
        String[] args;

        /**
         * 传入命令行参数数组构造
         * @param args
         */
        CmdArgs(String[] args) {
            this.args = args;
        }
        /**
         * 获得命令行参数值
         * @return boolean
         */
        public boolean checkCmd() {
            if(!args[0].equals("list")) {
                System.out.println("错误：命令不存在");
                return false;
            }
            if(!has()) {
                System.out.println("错误：命令缺少必要的参数");
                return false;
            }
            for(int i = 1; i < args.length; i++) {
                if (args[i].equals("-log")) {
                    i = getLogPath(i);
                    if (i == -1) {
                        System.out.println("错误：日志路径有误");
                        return false;
                    }
                } else if (args[i].equals("-out")) {
                    i = getOutPath(i);
                    if (i == -1) {
                        System.out.println("错误：输出路径有误");
                        return false;
                    }
                } else if (args[i].equals("-date")) {
                    i = getDate(i);
                    if (i == -1) {
                        System.out.println("错误：日期值格式有误");
                        return false;
                    }
                } else if (args[i].equals("-type")) {
                    i = getType(i);
                    if (i == -1) {
                        System.out.println("错误：指定格式值有误");
                        return false;
                    }
                } else if (args[i].equals("-province")) {
                    i = getProvince(i);
                    if (i == -1) {
                        System.out.println("错误：省份值有误");
                        return false;
                    }
                } else {
                    System.out.println("错误：未知参数");
                    return false;
                }
            }
            return true;
        }
        /**
         * 判断该命令是否有对应的必要参数
         * @return
         */
        boolean has() {
            if(Arrays.asList(args).contains("-log")&&Arrays.asList(args).contains("-out")) {
                return true;
            }
            return false;
        }
        /**
         * 得到日志文件位置
         * @param i
         * @return i
         */
        public int getLogPath(int i) {
            i++;
            if(i < args.length) {
                log_path = args[i];
            } else
                return -1;
            return i;
        }
        /**
         * 得到输出文件位置
         * @param i
         * @return i
         */
        public int getOutPath(int i) {
            i++;
            if(i < args.length) {
                out_path = args[i];
            } else
                return -1;
            return i;
        }
        /**
         * 得到日期
         * @param i
         * @return i
         */
        public int getDate(int i) {
            i++;
            if(i < args.length) {
                if(date.compareTo(args[i]) >= 0)
                    date = args[i];
                else
                    return -1;
            } else
                return -1;
            return i;
        }
        /**
         * 得到指定类型
         * @param i
         * @return i
         */
        public int getType(int i) {
            i++;
            int j = i;
            if(i < args.length) {
                while(i<args.length) {
                    if(args[i].equals("ip")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("sp")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("cure")) {
                        type_list.add(args[i]);
                        i++;
                    } else if(args[i].equals("dead")) {
                        type_list.add(args[i]);
                        i++;
                    } else
                        break;
                }
            }
            if(j == i)
                return -1;
            return (i - 1);
        }
        /**
         * 得到指定省份
         * @param i
         * @return i
         */
        public int getProvince(int i) {
            i++;
            int j = i;
            if(i < args.length) {
                while(i<args.length) {
                    if(Arrays.asList(province_str).contains(args[i])) {
                        province_list.add(args[i]);
                        i++;
                    } else
                        break;
                }
            }
            if(j == i)
                return -1;
            return (i - 1);
        }
    }

    class InfectFileManager {
        InfectFileManager() {}

        public void init() {
            ArrayList<province> result = new ArrayList<>();//完整的省份列表
            String content = readLog(log_path,date);//读取文件夹下的文件
            result = match(content);
            // System.out.println("\n");
            /*for(int i = 0; i < result.size(); i++){
            	System.out.println(result.get(i).printResult());
            }*/
            HashMap<Integer, province> result_map = new HashMap<>();
            result_map = sort(result);
            Set<Entry<Integer, province>> entries =result_map.entrySet();
            for(Entry<Integer, province> entry:entries ){
                System.out.println(entry.getKey());
                System.out.println(entry.getValue().printResult());
            }
            //outResult(result_map, type_list, province_list);
        }

        public String readLog(String filePath, String date) {
            try {
                File file = new File(filePath);
                File[] files = file.listFiles();
                StringBuilder content = new StringBuilder();
                Arrays.sort(files);
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile() && isearly(files[i].getName(),date)){
                        InputStreamReader reader = new InputStreamReader(new FileInputStream(files[i]));
                        BufferedReader br = new BufferedReader(reader);
                        String line="";
                        line = br.readLine();
                        while (line != null && !line.startsWith("//")){
                            content.append(System.lineSeparator() + line);
                            line = br.readLine();
                        }
                        br.close();
                    }
                }
                return content.toString();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private boolean isearly(String filename, String date) {
            date += ".log.txt";
            if (filename.compareTo(date) <= 0) { //如果该文件的日期小于指定日期
                return true;
            }
            return false;
        }

        public ArrayList<province> match(String content) {
            ArrayList<province> result = new ArrayList<>();
            String pattern1 = "(\\S+) 新增 感染患者 (\\d+)人";
            String pattern2 = "(\\S+) 新增 疑似患者 (\\d+)人";
            String pattern3 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
            String pattern4 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
            String pattern5 = "(\\S+) 死亡 (\\d+)人";
            String pattern6 = "(\\S+) 治愈 (\\d+)人";
            String pattern7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
            String pattern8 = "(\\S+) 排除 疑似患者 (\\d+)人";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
                String line = "";
                line = br.readLine();
                while ((line = br.readLine()) != null) {
                    Matcher matcher1 = Pattern.compile(pattern1).matcher(line);
                    Matcher matcher2 = Pattern.compile(pattern2).matcher(line);
                    Matcher matcher3 = Pattern.compile(pattern3).matcher(line);
                    Matcher matcher4 = Pattern.compile(pattern4).matcher(line);
                    Matcher matcher5 = Pattern.compile(pattern5).matcher(line);
                    Matcher matcher6 = Pattern.compile(pattern6).matcher(line);
                    Matcher matcher7 = Pattern.compile(pattern7).matcher(line);
                    Matcher matcher8 = Pattern.compile(pattern8).matcher(line);
                    while (matcher1.find()) {
                        result = addIp(result, matcher1);
                    }
                    while (matcher2.find()) {
                        result = addSp(result, matcher2);
                    }
                    while (matcher3.find()) {
                        result = moveIp(result, matcher3);
                    }
                    while (matcher4.find()) {
                        result = moveSp(result, matcher4);
                    }
                    while (matcher5.find()) {
                        result = addDead(result, matcher5);
                    }
                    while (matcher6.find()) {
                        result = addCure(result, matcher6);
                    }
                    while (matcher7.find()) {
                        result = diagnosisSp(result, matcher7);
                    }
                    while (matcher8.find()) {
                        result = excludeSp(result, matcher8);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        private ArrayList<province> addIp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(Integer.parseInt(matcher.group(2)) + result.get(i).getIp());
                }
            }
            if(!b) {//省份不存在
                province p =new province(matcher.group(1), Integer.parseInt(matcher.group(2)), 0, 0, 0);
                result.add(p);
            }
            return result;
        }

        private ArrayList<province> addSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setSp(Integer.parseInt(matcher.group(2)) + result.get(i).getSp());
                }
            }
            if(!b) {//省份不存在
                province p =new province(matcher.group(1), 0, Integer.parseInt(matcher.group(2)), 0, 0);
                result.add(p);
            }
            return result;
        }

        private ArrayList<province> moveIp(ArrayList<province> result, Matcher matcher) {
            int out = -1;//流出省
            int in = -1;//流入省
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    out = i;
                }
                if(result.get(i).getName().equals(matcher.group(2))){
                    in = i;
                }
            }
            if(out == -1) {//流出省份不存在
                System.out.println("流出省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            else {
                if(in == -1) {//流入省份不存在
                    province p =new province(matcher.group(2), Integer.parseInt(matcher.group(3)), 0, 0, 0);
                    result.add(p);
                    result.get(out).setIp(result.get(out).getIp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
                else {
                    result.get(in).setIp(result.get(in).getIp() + Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                    result.get(out).setIp(result.get(out).getIp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
            }
            return result;
        }

        private ArrayList<province> moveSp(ArrayList<province> result, Matcher matcher) {
            int out = -1;//流出省
            int in = -1;//流入省
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    out = i;
                }
                if(result.get(i).getName().equals(matcher.group(2))){
                    in = i;
                }
            }
            if(out == -1) {//流出省份不存在
                System.out.println("流出省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
            else {
                if(in == -1) {//流入省份不存在
                    province p =new province(matcher.group(2), 0, Integer.parseInt(matcher.group(3)), 0, 0);
                    result.add(p);
                    result.get(out).setSp(result.get(out).getSp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
                else {
                    result.get(in).setSp(result.get(in).getSp() + Integer.parseInt(matcher.group(3)));//修改流入省的感染患者人数
                    result.get(out).setSp(result.get(out).getSp() - Integer.parseInt(matcher.group(3)));//修改流出省的感染患者人数
                }
            }
            return result;
        }

        private ArrayList<province> addDead(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(result.get(i).getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    result.get(i).setDead(Integer.parseInt(matcher.group(2)) + result.get(i).getDead());//修改该省份的死亡人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("死亡省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            return result;
        }

        private ArrayList<province> addCure(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(result.get(i).getIp() - Integer.parseInt(matcher.group(2)));//修改该省份的感染患者人数
                    result.get(i).setCure(Integer.parseInt(matcher.group(2)) + result.get(i).getCure());//修改该省份的治愈人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("治愈省份" + matcher.group(1) + "不存在感染患者，数据有误");
            }
            return result;
        }

        private ArrayList<province> diagnosisSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setIp(Integer.parseInt(matcher.group(2)) + result.get(i).getIp());//修改该省份的感染患者人数
                    result.get(i).setSp(result.get(i).getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
            return result;
        }

        private ArrayList<province> excludeSp(ArrayList<province> result, Matcher matcher) {
            boolean b = false;
            for(int i = 0; i < result.size(); i++){
                if(result.get(i).getName().equals(matcher.group(1))){
                    b = true;
                    result.get(i).setSp(result.get(i).getSp() - Integer.parseInt(matcher.group(2)));//修改该省份的疑似患者人数
                }
            }
            if(!b) {//省份不存在
                System.out.println("确诊疑似省份" + matcher.group(1) + "不存在疑似患者，数据有误");
            }
            return result;
        }

        private HashMap<Integer, province> sort(ArrayList<province> result) {
            HashMap<Integer, province> result_map = new HashMap<>();
            int country_ip, country_sp, country_cure, country_dead;
            country_ip = country_sp = country_cure = country_dead = 0;
            for(int i = 0; i < result.size(); i++) {
                country_ip += result.get(i).getIp();
                country_sp += result.get(i).getSp();
                country_cure += result.get(i).getCure();
                country_dead += result.get(i).getDead();
                result_map.put(result.get(i).getPosition(), result.get(i));
            }
            province country = new province("全国", country_ip, country_sp, country_cure, country_dead);
            result_map.put(0, country);
            return result_map;
        }

        private void outResult(HashMap<Integer, province> result_map, ArrayList<String> type_list,
                               ArrayList<String> province_list) {

        }
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("错误：未输入参数");
            return;
        }
        InfectStatistic infectStatistic = new InfectStatistic();
        InfectStatistic.CmdArgs cmdargs = infectStatistic.new CmdArgs(args);
        if(!cmdargs.checkCmd()) {
            return;
        }
        InfectStatistic.InfectFileManager filemanager= infectStatistic.new InfectFileManager();
        filemanager.init();
    }

}


