package com.company;

import javax.swing.text.html.HTMLDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

class Vacancy {
    public Integer vacancy_id;
    public String lang;
    public String vacancy_name;
    public String priority;

    public Vacancy(Scanner sc){
        this.vacancy_id = Integer.valueOf(sc.next());
        this.lang = sc.next();
        this.vacancy_name = sc.next();
        this.priority = sc.next().split("$")[0];
    }

    @Override
    public String toString() {
        return vacancy_id + ";" + lang + ";" + vacancy_name + ";" + priority;
    }
}

class Recruiter{
    public Integer recruiter_id;
    public String recruiter_surname;
    public String recruiter_name;

    public Recruiter(Scanner sc){
        this.recruiter_id = Integer.valueOf(sc.next());
        this.recruiter_surname = sc.next();
        this.recruiter_name = sc.next().split("$")[0];
    }

    @Override
    public String toString(){
        return recruiter_id + ";" + recruiter_surname + ";" + recruiter_name;
    }
}

class Bonus{
    public String priority;
    public Integer days;
    public Integer bonus;

    public Bonus(Scanner sc){
        this.priority = sc.next();
        this.days = Integer.valueOf(sc.next());
        this.bonus = Integer.valueOf(sc.next().split("$")[0]);
    }

    @Override
    public String toString(){
        return priority + ";" + days + ";" + bonus;
    }
}

class NoClose{
    public Integer vancancy_id;
    public String data_open;
    public String data_close;
    public Integer recruiter_id;
    //public Date date;
    public Boolean is_act;
    public Integer days_delta;

    public NoClose(Scanner sc) throws ParseException {
        this.vancancy_id = Integer.valueOf(sc.next());
        this.data_open = sc.next();
        this.data_close = sc.next();
        this.recruiter_id = Integer.valueOf(sc.next().split("$")[0]);
        String strDate1 = this.data_open;
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        Date date1 = formatter1.parse(strDate1);
        LocalDate dateBefore90Days = LocalDate.now().minusDays(90);
        String tmp = dateBefore90Days.toString();
        String strDate2 = tmp;
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date2 = formatter2.parse(strDate2);
        /*
        Date date_strt = formatter1.parse(data_open);
        Date date_end = formatter1.parse(data_close);
        long milliseconds = date_end.getTime() - date_strt.getTime();
        this.days_delta = (int) (milliseconds / (24 * 60 * 60 * 1000));
        */
        /*

        long milliseconds = end_date.getTime() - strt_date.getTime();
        this.days_delta = (int) (milliseconds / (24 * 60 * 60 * 1000));
         */
        if(data_close.equals("0")){
            int c = date1.compareTo(date2);
            if(c == 1){
                this.is_act = Boolean.TRUE;
            }
            else{
                this.is_act = Boolean.FALSE;
            }
        }
        else{
            this.is_act = Boolean.TRUE;
        }

    }
    @Override
    public String toString(){
        return vancancy_id + ";" + data_open + ";" + data_close + ";" + recruiter_id;
    }
}

public class Main {

    public static void main(String[] args) throws IOException, FileNotFoundException, ParseException {
        Scanner sc1 = new Scanner(new File("input1.txt"));
        sc1.useDelimiter("[;\\n]");
        ArrayList<Vacancy> vac = new ArrayList<>();
        while(sc1.hasNext()){
            vac.add(new Vacancy(sc1));
        }
        ArrayList<Vacancy> vacancies = new ArrayList<>(vac);
        Comparator<Vacancy> cmp1 = new Comparator<Vacancy>() {
            @Override
            public int compare(Vacancy o1, Vacancy o2) {
                return o1.priority.compareTo(o2.priority);
            }
        };
        Collections.sort(vac, cmp1);
        PrintWriter pw1 = new PrintWriter(new File("output1.txt"));
        for(int i = 0; i < vac.size(); i++){
            if(i == vac.size() - 1){
                pw1.print(vac.get(i).vacancy_id + ";" + vac.get(i).vacancy_name + ";" + vac.get(i).priority);
                break;
            }
            pw1.println(vac.get(i).vacancy_id + ";" + vac.get(i).vacancy_name + ";" + vac.get(i).priority);
        }
        pw1.flush();

        Scanner sc2 = new Scanner(new File("input2.txt"));
        sc2.useDelimiter("[;\\n]");
        ArrayList<Recruiter> rec = new ArrayList<>();
        while(sc2.hasNext()){
            rec.add(new Recruiter(sc2));
        }

        Scanner sc3 = new Scanner(new File("input3.txt"));
        sc3.useDelimiter("[;\\n]");
        ArrayList<Bonus> bns = new ArrayList<>();
        while(sc3.hasNext()){
            bns.add(new Bonus(sc3));
        }

        Scanner sc4 = new Scanner(new File("input4.txt"));
        sc4.useDelimiter("[;\\n]");
        ArrayList<NoClose> no_close = new ArrayList<>();
        while(sc4.hasNext()){
            no_close.add(new NoClose(sc4));
        }
        HashSet<Integer> no_close_rec_id = new HashSet<>();
        for(int i = 0; i < no_close.size(); i++){
            if(no_close.get(i).data_close.equals("0")){
                no_close_rec_id.add(no_close.get(i).recruiter_id);
            }
        }
        Iterator<Integer> it = no_close_rec_id.iterator();
        PrintWriter pw3 = new PrintWriter(new File("output3.txt"));
        while(it.hasNext()){
            Integer tmp = it.next();
            for(int i = 0; i < rec.size(); i++){
                if(tmp.equals(rec.get(i).recruiter_id)){
                    pw3.print(rec.get(i).recruiter_surname + ";" + rec.get(i).recruiter_name);
                }
            }
            if(it.hasNext()){
                pw3.println();
            }
        }
        pw3.flush();

        ArrayList<Integer> no_act = new ArrayList<>();

        for(int i = 0; i < no_close.size(); i++){
            if(no_close.get(i).is_act.equals(Boolean.FALSE)){
                no_act.add(no_close.get(i).vancancy_id);
            }
        }
        PrintWriter pw2 = new PrintWriter(new File("output2.txt"));
        Collections.reverse(no_act);
        Iterator<Integer> it2 = no_act.iterator();
        while(it2.hasNext()){
            Integer tmp = it2.next();
            for(int i = 0; i < no_close.size(); i++){
                if(tmp.equals(no_close.get(i).vancancy_id)){
                    pw2.print(no_close.get(i).vancancy_id + ";" + no_close.get(i).data_open);
                }
            }
            if(it2.hasNext()){
                pw2.println();
            }
        }
        pw2.flush();

        Scanner sc5 = new Scanner(new File("input5.txt"));
        Integer recr_id = sc5.nextInt();

        //System.out.println(no_close);
        List<NoClose> no_cls = no_close.stream().filter(x-> x.recruiter_id.equals(recr_id) & !x.data_close.equals("0")).collect(Collectors.toList());
        ArrayList<Vacancy> vacfltr  = new ArrayList<>();
        for(int i = 0; i < no_cls.size(); i++){
            int finalI = i;
            Vacancy tmp = vacancies.stream().filter(x-> x.vacancy_id.equals(no_cls.get(finalI).vancancy_id)).collect(Collectors.toList()).get(0);
            vacfltr.add(tmp);
        }
        DaysDelta(no_cls);
        ArrayList<Bonus> bns_out = new ArrayList<>();
        for(int i = 0; i < no_cls.size(); i++){
            int finalI = i;
            if(bns.stream().filter(x-> x.priority.equals(vacfltr.get(finalI).priority) & no_cls.get(finalI).days_delta <= x.days).collect(Collectors.toList()).size() != 0){
                bns_out.add(bns.stream().filter(x-> x.priority.equals(vacfltr.get(finalI).priority) & no_cls.get(finalI).days_delta <= x.days).collect(Collectors.toList()).get(0));
            }
        }
        PrintWriter pw4 = new PrintWriter(new File("output4.txt"));
        Integer bns_sum = 0;
        for(int i = 0; i < bns_out.size(); i++){
            bns_sum += bns_out.get(i).bonus;
        }
        pw4.print(bns_sum);
        pw4.flush();


    }

    public static void DaysDelta(List<NoClose> no_cls) throws ParseException {
        for(int i = 0; i < no_cls.size(); i++){
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Date date_strt = formatter1.parse(no_cls.get(i).data_open);
            Date date_end = formatter1.parse(no_cls.get(i).data_close);
            long milliseconds = date_end.getTime() - date_strt.getTime();
            no_cls.get(i).days_delta = (int) (milliseconds / (24 * 60 * 60 * 1000));

        }
    }
}
