package com.wisee.service;

import com.wisee.model.KnowledgeFrame;
import java.util.*;

/**
 * Basis Pengetahuan WiSee — Sistem berbasis Frame
 * 100+ kata dalam 16 kategori bahasa Indonesia
 */
public class KnowledgeFrameService {

    private static KnowledgeFrameService instance;
    private final LinkedHashMap<String, KnowledgeFrame> frames = new LinkedHashMap<>();

    private KnowledgeFrameService() { buildAll(); }

    public static KnowledgeFrameService getInstance() {
        if (instance == null) instance = new KnowledgeFrameService();
        return instance;
    }

    private void f(String w, String cat, String emoji, String audio, String prompt, boolean safe) {
        frames.put(w.toUpperCase(), new KnowledgeFrame(w.toUpperCase(), cat, emoji, audio, prompt, safe));
    }

    private void buildAll() {

        // ══════════════════════════════════════════
        //  1. HEWAN (26 kata)
        // ══════════════════════════════════════════
        f("KUCING","hewan","🐱",
                "Kucing! Kucing adalah hewan berbulu yang suka mengeong. Meong meong! Kucing suka bermain dan minum susu.",
                "cute cartoon cat sitting, children book illustration, bright orange fur, white background",true);
        f("ANJING","hewan","🐶",
                "Anjing! Anjing adalah hewan yang pintar dan setia. Guk guk! Anjing suka bermain bola bersama kita.",
                "cute happy cartoon dog with wagging tail, children style, golden fur, white background",true);
        f("IKAN","hewan","🐟",
                "Ikan! Ikan bernapas dengan insang dan hidup di dalam air. Ikan berenang dengan sirip cantiknya.",
                "cute colorful tropical fish swimming, blue water, children book illustration, white background",true);
        f("BURUNG","hewan","🐦",
                "Burung! Burung punya sayap indah sehingga bisa terbang tinggi di langit biru. Cuit cuit!",
                "cute colorful cartoon bird on branch, red and yellow feathers, children style, white background",true);
        f("BABI","hewan","🐷",
                "Babi! Babi adalah hewan dengan hidung moncong yang suka makan. Ngok ngok! Berikan aku makan.",
                "cute colorful cartoon pig in pig pen, pink skin, children style, white background",true);
        f("AYAM","hewan","🐔",
                "Ayam! Ayam adalah hewan ternak yang rajin. Kukuruyuk! Ayam berkokok di pagi hari.",
                "cute cartoon chicken with chicks on farm, children style, colorful, white background",true);
        f("SAPI","hewan","🐮",
                "Sapi! Sapi suka makan rumput dan memberi kita susu yang lezat. Moo moo!",
                "cute cartoon cow with spots on green field, children style, black and white, white background",true);
        f("GAJAH","hewan","🐘",
                "Gajah! Gajah adalah hewan terbesar di darat. Belalainya panjang untuk mengambil makanan.",
                "cute cartoon elephant with big ears and long trunk, grey, children book style, white background",true);
        f("KELINCI","hewan","🐰",
                "Kelinci! Kelinci punya telinga panjang dan suka melompat. Kelinci suka makan wortel.",
                "cute white fluffy cartoon rabbit with long ears and carrot, children style, white background",true);
        f("HARIMAU","hewan","🐯",
                "Harimau! Harimau punya belang oranye hitam yang sangat keren. Harimau hewan yang kuat!",
                "cute cartoon tiger with orange black stripes sitting, children book style, white background",true);
        f("MONYET","hewan","🐒",
                "Monyet! Monyet suka berayun di pohon sambil makan pisang. Hii hii! Monyet sangat lincah.",
                "cute cartoon monkey swinging on tree with banana, brown, children style, white background",true);
        f("KUDA","hewan","🐴",
                "Kuda! Kuda adalah hewan yang kuat dan cepat berlari. Hieee! Kuda suka makan rumput.",
                "cute cartoon horse running in field, brown with white mane, children style, white background",true);
        f("BEBEK","hewan","🦆",
                "Bebek! Bebek suka berenang di kolam. Kwek kwek! Bebek punya kaki berselaput.",
                "cute yellow cartoon duck swimming in pond with ripples, children book style, white background",true);
        f("KUPU","hewan","🦋",
                "Kupu kupu! Kupu kupu punya sayap indah berwarna warni. Kupu kupu suka menghisap nektar bunga.",
                "cute colorful butterfly with big patterned wings on flower, children book illustration, white background",true);
        f("LEBAH","hewan","🐝",
                "Lebah! Lebah adalah serangga rajin pembuat madu. Madu lebah sangat manis dan menyehatkan!",
                "cute cartoon bee with yellow black stripes flying near flower, children style, white background",true);
        f("ULAT","hewan","🐛",
                "Ulat! Ulat adalah bayi dari kupu kupu. Ulat makan daun, lalu jadi kupu kupu cantik.",
                "cute green cartoon caterpillar on leaf, big eyes, children book style, white background",true);
        f("KATAK","hewan","🐸",
                "Katak! Katak suka melompat dan tinggal di dekat air. Katak bersuara koak koak!",
                "cute green cartoon frog jumping on lily pad, big eyes, children style, white background",true);
        f("KURA","hewan","🐢",
                "Kura kura! Kura kura punya cangkang keras sebagai pelindung tubuhnya. Jalannya pelan tapi pasti!",
                "cute cartoon turtle with patterned shell walking slowly, green, children style, white background",true);
        f("PENGUIN","hewan","🐧",
                "Penguin! Penguin adalah burung yang tidak bisa terbang tapi bisa berenang. Lucu sekali!",
                "cute cartoon penguin with black and white colors standing, children book style, white background",true);
        f("BERUANG","hewan","🐻",
                "Beruang! Beruang adalah hewan berbulu besar. Beruang suka makan madu dan ikan salmon.",
                "cute brown cartoon bear sitting with honey pot, children style, white background",true);
        f("RUSA","hewan","🦌",
                "Rusa! Rusa punya tanduk yang indah di kepalanya. Rusa berlari sangat kencang di hutan.",
                "cute cartoon deer with antlers in forest, brown, children book style, white background",true);
        f("KANGURU","hewan","🦘",
                "Kanguru! Kanguru punya kantung di perutnya untuk membawa bayinya. Kanguru bisa melompat jauh!",
                "cute cartoon kangaroo with baby in pouch, grey brown, children style, white background",true);
        f("JERAPAH","hewan","🦒",
                "Jerapah! Jerapah adalah hewan tertinggi di dunia. Lehernya sangat panjang untuk makan daun tinggi.",
                "cute cartoon giraffe with long neck and spots, yellow brown, children style, white background",true);
        f("ZEBRA","hewan","🦓",
                "Zebra! Zebra punya corak belang hitam putih yang sangat cantik. Zebra tinggal di savana Afrika.",
                "cute cartoon zebra with black white stripes, children book style, white background",true);
        f("SINGA","hewan","🦁",
                "Singa! Singa adalah raja hutan yang gagah dan kuat. Singa bersuara mengaum dengan keras!",
                "cute cartoon lion with fluffy mane roaring, golden yellow, children style, white background",true);
        f("BUAYA","hewan","🐊",
                "Buaya! Buaya adalah reptil besar yang hidup di air dan darat. Buaya punya gigi yang tajam!",
                "cute cartoon crocodile with big smile showing teeth, green, children style, white background",true);
        f("LUMBA","hewan","🐬",
                "Lumba lumba! Lumba lumba adalah hewan laut yang sangat cerdas dan suka bermain. Mereka sangat ramah!",
                "cute cartoon dolphin jumping in ocean waves, blue grey, children book style, white background",true);

        // ══════════════════════════════════════════
        //  2. BUAH (18 kata)
        // ══════════════════════════════════════════
        f("APEL","buah","🍎",
                "Apel! Apel berwarna merah dan rasanya manis segar. Apel mengandung banyak vitamin untuk tubuh.",
                "cute shiny red apple with green leaf and stem, cartoon style, white background",true);
        f("PISANG","buah","🍌",
                "Pisang! Pisang berwarna kuning dan rasanya manis. Pisang mengandung banyak energi!",
                "cute yellow banana bunch, ripe, cartoon style, white background",true);
        f("JERUK","buah","🍊",
                "Jeruk! Jeruk berbentuk bulat berwarna oranye. Jeruk mengandung banyak vitamin C!",
                "cute orange citrus fruit with leaf, cartoon style, bright, white background",true);
        f("MANGGA","buah","🥭",
                "Mangga! Mangga yang matang rasanya sangat manis. Mangga adalah raja buah tropis!",
                "cute ripe yellow mango with green top, cartoon style, tropical, white background",true);
        f("SEMANGKA","buah","🍉",
                "Semangka! Semangka warnanya merah di dalam, hijau di luar. Sangat segar dimakan saat panas!",
                "cute watermelon slice showing red flesh with seeds, green rind, cartoon style, white background",true);
        f("ANGGUR","buah","🍇",
                "Anggur! Anggur berbentuk bulat kecil tumbuh dalam tandan yang lebat. Rasanya manis!",
                "cute purple grape bunch with green leaves, shiny, cartoon style, white background",true);
        f("NANAS","buah","🍍",
                "Nanas! Nanas punya mahkota daun di atasnya. Nanas rasanya asam manis menyegarkan!",
                "cute pineapple with crown leaves, yellow spiky outside, cartoon style, white background",true);
        f("STROBERI","buah","🍓",
                "Stroberi! Stroberi berwarna merah dengan bintik kecil. Rasanya manis asam yang lezat!",
                "cute red strawberry with green stem and seeds, heart shaped, cartoon style, white background",true);
        f("PEPAYA","buah","🫐",
                "Pepaya! Pepaya adalah buah tropis rasanya manis dengan daging berwarna oranye. Baik untuk perut!",
                "cute papaya cut in half showing orange flesh and black seeds, cartoon style, white background",true);
        f("MELON","buah","🍈",
                "Melon! Melon rasanya sangat manis dan mengandung banyak air. Melon baik dimakan saat siang!",
                "cute green melon with yellow flesh slice, cartoon style, white background",true);
        f("ALPUKAT","buah","🥑",
                "Alpukat! Alpukat berwarna hijau dan dagingnya lembut. Alpukat mengandung lemak baik untuk tubuh.",
                "cute avocado cut in half with big seed, green outside creamy inside, cartoon style, white background",true);
        f("KELAPA","buah","🥥",
                "Kelapa! Kelapa menghasilkan air kelapa yang segar dan daging kelapa yang lezat.",
                "cute coconut cut in half showing white flesh and water, cartoon style, tropical, white background",true);
        f("RAMBUTAN","buah","🍒",
                "Rambutan! Rambutan adalah buah tropis khas Indonesia. Kulitnya berbulu merah dengan daging putih manis.",
                "cute red rambutan fruit with hair like spines, tropical fruit, cartoon style, white background",true);
        f("DURIAN","buah","🌵",
                "Durian! Durian adalah raja buah dengan kulit berduri tajam. Baunya khas dan rasanya sangat lezat!",
                "cute cartoon durian fruit with spiky shell, yellow inside, king of fruits, children style, white background",true);
        f("SALAK","buah","🐍",
                "Salak! Salak adalah buah khas Indonesia dengan kulit bersisik seperti ular. Rasanya manis asam.",
                "cute cartoon salak fruit with scaly brown skin, tropical Indonesian fruit, children style, white background",true);
        f("JAMBU","buah","🍑",
                "Jambu! Jambu air rasanya segar dan manis. Jambu biji berwarna merah mengandung banyak vitamin C!",
                "cute guava fruit pink and green, tropical, cartoon style, white background",true);
        f("LEMON","buah","🍋",
                "Lemon! Lemon berwarna kuning cerah dan rasanya asam. Lemon baik untuk minuman segar dan kesehatan.",
                "cute bright yellow lemon with leaves, sour citrus, cartoon style, white background",true);
        f("CERI","buah","🍒",
                "Ceri! Ceri adalah buah kecil berwarna merah atau hitam yang manis. Bentuknya bulat mungil.",
                "cute red cherries pair with green stems, shiny, cartoon style, white background",true);

        // ══════════════════════════════════════════
        //  3. SAYURAN (12 kata)
        // ══════════════════════════════════════════
        f("WORTEL","sayuran","🥕",
                "Wortel! Wortel berwarna oranye cerah. Wortel sangat baik untuk kesehatan mata. Disukai kelinci!",
                "cute cartoon carrot with green leafy top, orange, white background",true);
        f("TOMAT","sayuran","🍅",
                "Tomat! Tomat berwarna merah bulat segar. Tomat bisa dibuat sambal, saus, dan jus yang lezat.",
                "cute round red tomato with green stem, shiny, cartoon style, white background",true);
        f("JAGUNG","sayuran","🌽",
                "Jagung! Jagung berwarna kuning cerah dengan bulir yang tersusun rapi. Enak dimakan bakar!",
                "cute yellow corn on the cob with green husk, cartoon style, white background",true);
        f("BAYAM","sayuran","🥬",
                "Bayam! Bayam adalah sayuran hijau yang kaya zat besi. Makan bayam membuat tubuh kuat!",
                "cute green spinach leaves in bunch, fresh, cartoon style, white background",true);
        f("KANGKUNG","sayuran","🌿",
                "Kangkung! Kangkung adalah sayuran hijau khas Asia Tenggara. Kangkung tumis sangat lezat!",
                "cute green water spinach kangkung leaves, tropical vegetable, cartoon style, white background",true);
        f("KUBIS","sayuran","🥦",
                "Kubis! Kubis berbentuk bulat dengan banyak lapisan daun. Kubis baik untuk kesehatan pencernaan.",
                "cute round green cabbage, layered leaves, cartoon style, white background",true);
        f("KENTANG","sayuran","🥔",
                "Kentang! Kentang bisa diolah menjadi berbagai makanan lezat. Kentang goreng! Kentang rebus!",
                "cute brown potato with eyes, round, cartoon style, white background",true);
        f("TIMUN","sayuran","🥒",
                "Timun! Timun rasanya segar dan banyak mengandung air. Timun baik untuk kesehatan kulit.",
                "cute green cucumber with slices showing seeds inside, cartoon style, white background",true);
        f("BROKOLI","sayuran","🥦",
                "Brokoli! Brokoli berwarna hijau seperti pohon kecil. Brokoli mengandung banyak vitamin dan mineral.",
                "cute green broccoli like small tree, cartoon style, white background",true);
        f("TERONG","sayuran","🍆",
                "Terong! Terong berwarna ungu mengkilap. Terong bisa dimasak menjadi lauk yang lezat.",
                "cute purple eggplant with green stem, shiny, cartoon style, white background",true);
        f("LABU","sayuran","🎃",
                "Labu! Labu berwarna oranye besar. Labu bisa dibuat sup dan kue yang manis.",
                "cute orange pumpkin with green stem, round, cartoon style, white background",true);
        f("BAWANG","sayuran","🧅",
                "Bawang! Bawang dipakai sebagai bumbu masak yang penting. Bawang membuat masakan jadi harum!",
                "cute onion with layers showing, brown with purple tint, cartoon style, white background",true);

        // ══════════════════════════════════════════
        //  4. BENDA SEHARI-HARI (15 kata)
        // ══════════════════════════════════════════
        f("BUKU","benda","📚",
                "Buku! Buku adalah jendela dunia. Dengan membaca buku kita bisa belajar banyak hal baru. Ayo baca!",
                "colorful open storybook with magic sparkles and pictures, cartoon children style, white background",true);
        f("PENSIL","benda","✏️",
                "Pensil! Pensil adalah teman setia belajar kita. Dengan pensil kita bisa menulis dan menggambar.",
                "cute yellow pencil with pink eraser smiling face, cartoon style, white background",true);
        f("PENGGARIS","benda","📏",
                "Penggaris! Penggaris dipakai untuk mengukur panjang dan membuat garis lurus yang rapi.",
                "cute cartoon ruler with measurements, colorful, children style, white background",true);
        f("GUNTING","benda","✂️",
                "Gunting! Gunting dipakai untuk memotong kertas dan kain. Hati hati saat menggunakan gunting ya!",
                "cute cartoon scissors with happy face, colorful handles, children style, white background",true);
        f("MEJA","benda","🪑",
                "Meja! Meja adalah tempat kita belajar dan mengerjakan tugas sekolah. Jaga mejamu bersih!",
                "cute cartoon wooden desk with books and pencils on top, children style, white background",true);
        f("KURSI","benda","🪑",
                "Kursi! Kursi adalah tempat duduk yang nyaman. Duduklah dengan tegak agar punggungmu sehat!",
                "cute cartoon colorful chair with cushion, children style, white background",true);
        f("RUMAH","benda","🏠",
                "Rumah! Rumah adalah tempat tinggal bersama keluarga tercinta. Rumah tempat paling nyaman.",
                "cute colorful cartoon house with garden flowers, red roof, white walls, children style, white background",true);
        f("SEPATU","benda","👟",
                "Sepatu! Sepatu melindungi kaki kita saat berjalan. Jangan lupa pakai sepatu saat keluar rumah!",
                "cute colorful sneakers cartoon, red and white, with laces, children style, white background",true);
        f("TAS","benda","🎒",
                "Tas! Tas sekolah dipakai untuk membawa buku dan alat tulis. Rawat tasmu agar selalu bersih!",
                "cute cartoon school backpack with colorful design, rainbow colors, children style, white background",true);
        f("BOLA","benda","⚽",
                "Bola! Bola dipakai untuk bermain olahraga yang menyenangkan. Bermain bola baik untuk kesehatan!",
                "cute cartoon soccer ball with black and white pattern, bouncing, children style, white background",true);
        f("PAYUNG","benda","☂️",
                "Payung! Payung dipakai saat hujan agar tidak basah. Payung berwarna warni sangat cantik!",
                "cute colorful cartoon umbrella with rain drops, open umbrella, children style, white background",true);
        f("KACAMATA","benda","👓",
                "Kacamata! Kacamata membantu orang yang penglihatannya kurang jelas. Kacamata melindungi mata juga.",
                "cute cartoon glasses with round lenses, colorful frame, children style, white background",true);
        f("JAM","benda","⏰",
                "Jam! Jam digunakan untuk melihat waktu. Ada jam dinding, jam tangan, dan jam alarm yang lucu.",
                "cute cartoon alarm clock showing time, ringing, children style, white background",true);
        f("CERMIN","benda","🪞",
                "Cermin! Cermin digunakan untuk melihat penampilan kita. Cek apakah rambut sudah rapi!",
                "cute cartoon mirror with decorative frame, reflection, children style, white background",true);
        f("LAMPU","benda","💡",
                "Lampu! Lampu memberi cahaya saat malam hari. Tanpa lampu ruangan akan gelap gulita.",
                "cute cartoon light bulb glowing with rays, yellow warm light, children style, white background",true);

        // ══════════════════════════════════════════
        //  5. KENDARAAN (10 kata)
        // ══════════════════════════════════════════
        f("MOBIL","kendaraan","🚗",
                "Mobil! Mobil adalah kendaraan beroda empat yang berjalan di jalan raya. Pakai sabuk pengaman!",
                "cute red cartoon car driving on road with wheels, children style, white background",true);
        f("SEPEDA","kendaraan","🚲",
                "Sepeda! Bersepeda adalah olahraga yang menyenangkan dan menyehatkan. Pakai helm saat bersepeda!",
                "cute colorful bicycle with basket and bell, cartoon style, children, white background",true);
        f("MOTOR","kendaraan","🏍️",
                "Motor! Motor adalah kendaraan beroda dua yang gesit. Selalu pakai helm dan berkendara hati hati!",
                "cute cartoon motorcycle with colorful design, children style, white background",true);
        f("BUS","kendaraan","🚌",
                "Bus! Bus adalah kendaraan besar yang bisa membawa banyak penumpang. Bus sekolah berwarna kuning!",
                "cute yellow school bus cartoon, windows with waving children, children style, white background",true);
        f("KERETA","kendaraan","🚂",
                "Kereta! Kereta api berjalan di atas rel dengan sangat cepat. Tut tut! Kereta membawa banyak orang.",
                "cute cartoon train on railroad tracks with steam, colorful wagons, children style, white background",true);
        f("PESAWAT","kendaraan","✈️",
                "Pesawat! Pesawat terbang tinggi di langit biru membawa kita ke tempat yang jauh dengan cepat.",
                "cute cartoon airplane in blue sky with clouds and contrail, children style, white background",true);
        f("KAPAL","kendaraan","🚢",
                "Kapal! Kapal berlayar di atas lautan luas. Kapal mengangkut penumpang dan barang antar pulau.",
                "cute cartoon ship sailing on ocean waves, big white vessel, children style, white background",true);
        f("HELIKOPTER","kendaraan","🚁",
                "Helikopter! Helikopter bisa terbang ke atas langsung dan melayang di tempat. Sangat keren!",
                "cute cartoon helicopter flying with spinning rotor, colorful, children style, white background",true);
        f("TRAKTOR","kendaraan","🚜",
                "Traktor! Traktor adalah kendaraan besar untuk membajak sawah dan ladang. Pak tani pakai traktor!",
                "cute green cartoon tractor on farm field, big wheels, children style, white background",true);
        f("AMBULANS","kendaraan","🚑",
                "Ambulans! Ambulans membawa orang yang sakit ke rumah sakit dengan cepat. Sirene berbunyi kencang!",
                "cute cartoon ambulance with red cross and sirens, white red, children style, white background",true);

        // ══════════════════════════════════════════
        //  6. ALAM (12 kata)
        // ══════════════════════════════════════════
        f("MATAHARI","alam","☀️",
                "Matahari! Matahari bersinar terang memberikan cahaya dan kehangatan. Sumber energi kehidupan kita!",
                "cute smiling cartoon sun with rays all around, bright yellow orange, children style, white background",true);
        f("BULAN","alam","🌙",
                "Bulan! Bulan muncul di malam hari menerangi kegelapan. Bulan berubah bentuk setiap harinya.",
                "cute cartoon crescent moon with stars in dark blue night sky, children style, white background",true);
        f("BINTANG","alam","⭐",
                "Bintang! Bintang berkilau indah di langit malam. Ada berjuta juta bintang di langit yang luas.",
                "cute glowing yellow five pointed star with sparkles, children style, dark blue background",true);
        f("AWAN","alam","☁️",
                "Awan! Awan terbentuk dari uap air yang naik ke langit. Awan putih terlihat seperti kapas besar.",
                "cute fluffy white clouds in clear blue sky, soft and puffy, children cartoon style, white background",true);
        f("HUJAN","alam","🌧️",
                "Hujan! Hujan turun dari langit membasahi bumi. Hujan membuat tanaman tumbuh subur dan udara segar.",
                "cute rain drops falling from dark cloud, child with yellow umbrella below, children style, white background",true);
        f("PELANGI","alam","🌈",
                "Pelangi! Pelangi muncul setelah hujan dengan warna merah oranye kuning hijau biru dan ungu. Indah!",
                "cute colorful rainbow over green hills after rain, children book illustration, white background",true);
        f("POHON","alam","🌳",
                "Pohon! Pohon yang rindang membuat udara sejuk dan segar. Pohon adalah rumah bagi banyak hewan.",
                "big green cartoon tree with round canopy, birds on branches, children book style, white background",true);
        f("BUNGA","alam","🌸",
                "Bunga! Bunga sangat cantik dan berwarna warni. Bunga harum baunya dan menjadi makanan lebah.",
                "beautiful colorful blooming flowers in garden, tulips roses daisies, cartoon style, white background",true);
        f("GUNUNG","alam","⛰️",
                "Gunung! Gunung adalah dataran yang tinggi menjulang ke langit. Dikelilingi hutan hijau yang lebat.",
                "cute cartoon mountain peaks with snow on top and green forest below, children style, white background",true);
        f("LAUT","alam","🌊",
                "Laut! Laut adalah perairan yang sangat luas dan dalam. Di dalam laut hidup banyak ikan!",
                "cute cartoon ocean waves with fish and coral visible, beach in distance, children style, white background",true);
        f("SUNGAI","alam","🏞️",
                "Sungai! Sungai adalah aliran air yang mengalir dari gunung ke laut. Sungai adalah sumber air kita.",
                "cute cartoon river flowing through green forest with fish and ducks, children style, white background",true);
        f("SAWAH","alam","🌾",
                "Sawah! Sawah adalah tempat menanam padi. Pak tani bekerja keras di sawah agar kita bisa makan nasi.",
                "cute cartoon rice field terraces with green plants and blue sky, children style, white background",true);

        // ══════════════════════════════════════════
        //  7. ANGGOTA TUBUH (10 kata)
        // ══════════════════════════════════════════
        f("MATA","tubuh","👁️",
                "Mata! Mata kita gunakan untuk melihat keindahan dunia. Jaga matamu dengan istirahat yang cukup!",
                "cute cartoon big bright eyes with long lashes, children style, white background",true);
        f("TANGAN","tubuh","✋",
                "Tangan! Tangan kita gunakan untuk menulis, menggambar, dan memeluk orang yang kita sayangi.",
                "cute cartoon hand waving hello, colorful, children style, white background",true);
        f("KAKI","tubuh","🦶",
                "Kaki! Kaki kita gunakan untuk berjalan, berlari, dan melompat. Rawat kakimu selalu bersih!",
                "cute cartoon feet with colorful socks and shoes, children style, white background",true);
        f("HIDUNG","tubuh","👃",
                "Hidung! Hidung kita gunakan untuk mencium aroma yang harum. Hidung juga membantu kita bernapas.",
                "cute cartoon nose with flower scent lines, children style, white background",true);
        f("MULUT","tubuh","😊",
                "Mulut! Mulut kita gunakan untuk berbicara, makan, dan tersenyum. Gosok gigi dua kali sehari!",
                "cute cartoon smiling mouth with white teeth, children style, white background",true);
        f("TELINGA","tubuh","👂",
                "Telinga! Telinga kita gunakan untuk mendengar suara indah di sekitar kita. Jaga kesehatannya!",
                "cute cartoon ear with music notes floating, children style, white background",true);
        f("RAMBUT","tubuh","💇",
                "Rambut! Rambut tumbuh di kepala kita. Cuci dan sisir rambutmu setiap hari agar bersih dan rapi!",
                "cute cartoon child with curly colorful hair, shiny, children style, white background",true);
        f("GIGI","tubuh","🦷",
                "Gigi! Gigi kita gunakan untuk mengunyah makanan. Sikat gigi dua kali sehari agar gigi sehat!",
                "cute cartoon tooth with sparkles and toothbrush, white shiny, children style, white background",true);
        f("JARI","tubuh","☝️",
                "Jari! Kita punya sepuluh jari di kedua tangan. Jari digunakan untuk memegang dan merasakan.",
                "cute cartoon fingers counting one two three, colorful, children style, white background",true);
        f("PERUT","tubuh","🫃",
                "Perut! Perut kita gunakan untuk mencerna makanan. Makan makanan bergizi agar perut selalu sehat!",
                "cute cartoon belly with smiley face, round and happy, children style, white background",true);

        // ══════════════════════════════════════════
        //  8. WARNA (8 kata)
        // ══════════════════════════════════════════
        f("MERAH","warna","🔴",
                "Merah! Warna merah adalah warna cerah dan menarik perhatian. Apel, tomat, dan stroberi berwarna merah!",
                "big bold red circle on white background, with examples apples tomatoes, children learning style",true);
        f("BIRU","warna","🔵",
                "Biru! Warna biru seperti warna langit dan lautan yang luas. Biru memberikan rasa tenang.",
                "big bold blue circle on white background, with examples sky ocean, children learning style",true);
        f("HIJAU","warna","🟢",
                "Hijau! Warna hijau seperti warna daun dan rumput. Hijau melambangkan alam yang segar dan sehat.",
                "big bold green circle on white background, with examples leaves grass trees, children learning style",true);
        f("KUNING","warna","🟡",
                "Kuning! Warna kuning seperti matahari yang bersinar cerah. Pisang dan bintang berwarna kuning!",
                "big bold yellow circle on white background, with examples sun banana star, children learning style",true);
        f("UNGU","warna","🟣",
                "Ungu! Warna ungu adalah perpaduan merah dan biru yang cantik. Anggur dan lavender berwarna ungu.",
                "big bold purple circle on white background, with examples grapes lavender, children learning style",true);
        f("ORANYE","warna","🟠",
                "Oranye! Warna oranye seperti warna jeruk yang cerah. Oranye adalah warna semangat dan keceriaan!",
                "big bold orange circle on white background, with examples oranges pumpkin, children learning style",true);
        f("PUTIH","warna","⬜",
                "Putih! Warna putih bersih dan cerah. Awan, kapas, dan susu berwarna putih yang bersih.",
                "big white circle with border on light background, with examples clouds cotton milk, children learning style",true);
        f("HITAM","warna","⬛",
                "Hitam! Warna hitam adalah warna gelap. Langit malam dan biji mata kita berwarna hitam.",
                "big bold black circle on white background, night sky theme, children learning style",true);

        // ══════════════════════════════════════════
        //  9. MAKANAN (12 kata)
        // ══════════════════════════════════════════
        f("NASI","makanan","🍚",
                "Nasi! Nasi adalah makanan pokok kita sehari hari. Nasi mengandung karbohidrat sebagai sumber energi.",
                "cute bowl of fluffy white rice with chopsticks, steam rising, cartoon style, white background",true);
        f("ROTI","makanan","🍞",
                "Roti! Roti adalah makanan lezat untuk sarapan pagi. Roti bisa diberi selai coklat yang manis!",
                "cute cartoon bread loaf and slices, golden brown, with butter, children style, white background",true);
        f("SUSU","makanan","🥛",
                "Susu! Susu mengandung kalsium untuk tulang dan gigi yang kuat. Minum susu setiap hari yuk!",
                "cute cartoon glass of white milk with straw, splashing drops, children style, white background",true);
        f("TELUR","makanan","🥚",
                "Telur! Telur adalah makanan bergizi tinggi. Telur bisa dimasak menjadi berbagai hidangan lezat.",
                "cute cartoon egg with happy face, cracked showing yolk, children style, white background",true);
        f("MIE","makanan","🍜",
                "Mie! Mie adalah makanan yang lezat dan mengenyangkan. Mie goreng dan mie rebus sama sama enak!",
                "cute bowl of noodles with chopsticks, steam rising, colorful toppings, cartoon style, white background",true);
        f("SATE","makanan","🍢",
                "Sate! Sate adalah makanan khas Indonesia yang lezat. Daging ditusuk lidi lalu dibakar dengan bumbu.",
                "cute cartoon sate skewers grilling with peanut sauce, Indonesian food, children style, white background",true);
        f("TEMPE","makanan","🫘",
                "Tempe! Tempe adalah makanan khas Indonesia yang bergizi. Tempe terbuat dari kedelai yang difermentasi.",
                "cute cartoon tempe slices with soybean background, Indonesian food, children style, white background",true);
        f("TAHU","makanan","🍱",
                "Tahu! Tahu adalah makanan bergizi terbuat dari kedelai. Tahu goreng sangat renyah dan lezat!",
                "cute cartoon tofu block and fried tofu pieces, healthy food, cartoon style, white background",true);
        f("KUE","makanan","🎂",
                "Kue! Kue sangat lezat dan sering disajikan saat ulang tahun. Kue dibuat dari tepung dan gula.",
                "cute colorful birthday cake with candles and sprinkles, cartoon children style, white background",true);
        f("COKLAT","makanan","🍫",
                "Coklat! Coklat rasanya manis dan lezat. Tapi jangan terlalu banyak makan coklat ya!",
                "cute cartoon chocolate bar with pieces broken off, milk chocolate, children style, white background",true);
        f("PERMEN","makanan","🍭",
                "Permen! Permen rasanya manis dan ada banyak rasa dan warna. Jangan terlalu banyak makan permen!",
                "cute colorful candy lollipop and hard candies, rainbow colors, cartoon children style, white background",true);
        f("ES","makanan","🍦",
                "Es krim! Es krim dingin dan manis sangat lezat dimakan saat hari panas. Stroberi coklat vanila!",
                "cute cartoon ice cream cone with scoops of different flavors, dripping, children style, white background",true);

        // ══════════════════════════════════════════
        //  10. TEMPAT (10 kata)
        // ══════════════════════════════════════════
        f("SEKOLAH","tempat","🏫",
                "Sekolah! Sekolah adalah tempat kita belajar dan bertemu teman teman. Ayo rajin ke sekolah!",
                "cute cartoon school building with flag, colorful, students playing outside, children style, white background",true);
        f("RUMAH SAKIT","tempat","🏥",
                "Rumah sakit! Rumah sakit adalah tempat orang sakit dirawat dan disembuhkan oleh dokter dan perawat.",
                "cute cartoon hospital building with red cross, ambulance outside, children style, white background",true);
        f("PASAR","tempat","🏪",
                "Pasar! Pasar adalah tempat orang berjualan dan membeli berbagai kebutuhan sehari hari.",
                "cute cartoon traditional market with fruit vegetable stalls, colorful, children style, white background",true);
        f("TAMAN","tempat","🌳",
                "Taman! Taman adalah tempat yang indah untuk bermain dan bersantai bersama keluarga.",
                "cute cartoon park with trees flowers playground and benches, colorful, children style, white background",true);
        f("MASJID","tempat","🕌",
                "Masjid! Masjid adalah tempat umat Islam beribadah. Masjid tempat yang tenang dan damai.",
                "cute cartoon mosque with blue dome crescent and minaret, children style, white background",true);
        f("GEREJA","tempat","⛪",
                "Gereja! Gereja adalah tempat umat Kristen beribadah. Gereja memiliki menara dan lonceng.",
                "cute cartoon church building with cross and bell tower, children style, white background",true);
        f("PERPUSTAKAAN","tempat","📚",
                "Perpustakaan! Perpustakaan adalah tempat menyimpan banyak buku. Ayo membaca di perpustakaan!",
                "cute cartoon library with bookshelves full of colorful books, children reading, children style, white background",true);
        f("KEBUN BINATANG","tempat","🦁",
                "Kebun binatang! Kebun binatang adalah tempat kita bisa melihat berbagai macam hewan dari seluruh dunia.",
                "cute cartoon zoo with animal enclosures lion elephant giraffe, children style, white background",true);
        f("PANTAI","tempat","🏖️",
                "Pantai! Pantai adalah tempat yang indah dengan pasir putih dan ombak laut. Asyik bermain di pantai!",
                "cute cartoon beach with white sand blue waves umbrella and sandcastle, children style, white background",true);
        f("LAPANGAN","tempat","⚽",
                "Lapangan! Lapangan adalah tempat bermain olahraga bersama teman. Bermain bola di lapangan asyik!",
                "cute cartoon sports field with grass and goal posts, children playing, children style, white background",true);

        // ══════════════════════════════════════════
        //  11. PROFESI (10 kata)
        // ══════════════════════════════════════════
        f("GURU","profesi","👨‍🏫",
                "Guru! Guru adalah pahlawan tanpa tanda jasa. Guru mengajarkan kita ilmu pengetahuan. Hormati gurumu!",
                "cute cartoon teacher at blackboard with books and apple, children style, white background",true);
        f("DOKTER","profesi","👨‍⚕️",
                "Dokter! Dokter merawat dan menyembuhkan orang yang sakit. Dokter adalah profesi yang sangat mulia!",
                "cute cartoon doctor with stethoscope and white coat, children style, white background",true);
        f("POLISI","profesi","👮",
                "Polisi! Polisi menjaga keamanan dan ketertiban masyarakat. Polisi adalah pelindung kita semua!",
                "cute cartoon police officer with uniform and hat, friendly smile, children style, white background",true);
        f("PETANI","profesi","👨‍🌾",
                "Petani! Petani bekerja keras menanam padi dan sayuran untuk kita makan. Terima kasih pak tani!",
                "cute cartoon farmer with hat working in rice field, children style, white background",true);
        f("PILOT","profesi","👨‍✈️",
                "Pilot! Pilot adalah pengemudi pesawat terbang. Pilot harus belajar keras untuk menerbangkan pesawat.",
                "cute cartoon pilot in uniform with wings badge waving, children style, white background",true);
        f("KOKI","profesi","👨‍🍳",
                "Koki! Koki adalah ahli memasak yang membuat makanan lezat di restoran dan hotel. Yum yum!",
                "cute cartoon chef with tall hat and apron cooking, children style, white background",true);
        f("PEMADAM","profesi","👨‍🚒",
                "Pemadam kebakaran! Pemadam kebakaran memadamkan api dan menyelamatkan orang dalam kebakaran. Pemberani!",
                "cute cartoon firefighter with helmet hose and fire truck, children style, white background",true);
        f("NELAYAN","profesi","🎣",
                "Nelayan! Nelayan bekerja menangkap ikan di laut. Nelayan pergi melaut sejak pagi hari.",
                "cute cartoon fisherman on boat with net and fish, ocean, children style, white background",true);
        f("ARSITEK","profesi","👷",
                "Arsitek! Arsitek merancang bangunan rumah, sekolah, dan gedung yang kita gunakan sehari hari.",
                "cute cartoon architect with blueprint and hard hat, children style, white background",true);
        f("ASTRONOT","profesi","👨‍🚀",
                "Astronot! Astronot adalah penjelajah luar angkasa yang pergi ke bulan dan bintang. Sangat keren!",
                "cute cartoon astronaut in space suit floating in space with stars, children style, white background",true);

        // ══════════════════════════════════════════
        //  12. KELUARGA (8 kata)
        // ══════════════════════════════════════════
        f("AYAH","keluarga","👨",
                "Ayah! Ayah adalah kepala keluarga yang bekerja keras untuk keluarga tercinta. Sayangi ayahmu!",
                "cute cartoon father with child on shoulders, warm happy family, children style, white background",true);
        f("IBU","keluarga","👩",
                "Ibu! Ibu adalah pahlawan keluarga yang penuh kasih sayang. Ibu selalu ada untuk kita. Sayangi ibu!",
                "cute cartoon mother hugging child, warm loving, children style, white background",true);
        f("KAKAK","keluarga","👧",
                "Kakak! Kakak adalah saudara yang lebih tua. Hormati kakakmu dan bermainlah bersama dengan rukun.",
                "cute cartoon older sibling holding hands with younger sibling, children style, white background",true);
        f("ADIK","keluarga","👶",
                "Adik! Adik adalah saudara yang lebih muda yang perlu kita jaga. Sayangi adikmu dengan baik.",
                "cute cartoon baby or toddler with toy blocks, children style, white background",true);
        f("KAKEK","keluarga","👴",
                "Kakek! Kakek adalah ayah dari ayah atau ibu kita. Kakek banyak punya cerita dan pengalaman.",
                "cute cartoon grandfather with white hair and grandchild, warm, children style, white background",true);
        f("NENEK","keluarga","👵",
                "Nenek! Nenek adalah ibu dari ayah atau ibu kita. Nenek suka memasak makanan yang enak.",
                "cute cartoon grandmother knitting with grandchild, warm cozy, children style, white background",true);
        f("PAMAN","keluarga","👨‍👦",
                "Paman! Paman adalah saudara laki laki dari ayah atau ibu kita. Paman sering memberi hadiah!",
                "cute cartoon uncle with nephew niece, friendly, children style, white background",true);
        f("BIBI","keluarga","👩‍👦",
                "Bibi! Bibi adalah saudara perempuan dari ayah atau ibu kita. Bibi sangat menyayangi keponakan.",
                "cute cartoon aunt with nephew niece, friendly warm, children style, white background",true);


        // ══════════════════════════════════════════════════════════
        //  TAMBAHAN KNOWLEDGE BASE — paste di dalam buildAll()
        //  tepat sebelum baris penutup `}` dari buildAll()
        // ══════════════════════════════════════════════════════════

        // ══════════════════════════════════════════
        //  14. OLAHRAGA (10 kata) — BARU
        // ══════════════════════════════════════════
        f("SEPAKBOLA","olahraga","⚽",
                "Sepak bola! Sepak bola adalah olahraga paling populer di dunia. Tendang bola masuk ke gawang!",
                "cute cartoon soccer players kicking ball into goal, colorful jerseys, children style, white background",true);
        f("RENANG","olahraga","🏊",
                "Renang! Renang adalah olahraga yang menyenangkan di dalam air. Renang menyehatkan seluruh tubuh!",
                "cute cartoon child swimming in pool with goggles and swim cap, blue water, children style, white background",true);
        f("BASKET","olahraga","🏀",
                "Bola basket! Bola basket dimainkan dengan memasukkan bola ke dalam keranjang tinggi. Seru!",
                "cute cartoon child shooting basketball into hoop, orange ball, children style, white background",true);
        f("BADMINTON","olahraga","🏸",
                "Badminton! Badminton dimainkan dengan raket dan kok. Olahraga favorit di Indonesia!",
                "cute cartoon children playing badminton with rackets and shuttlecock, children style, white background",true);
        f("LARI","olahraga","🏃",
                "Berlari! Berlari adalah olahraga paling mudah dilakukan. Lari pagi membuat tubuh sehat dan segar!",
                "cute cartoon child running fast in park, happy expression, wind effect, children style, white background",true);
        f("SENAM","olahraga","🤸",
                "Senam! Senam membuat tubuh lentur dan kuat. Senam bisa dilakukan sendirian atau bersama-sama.",
                "cute cartoon child doing gymnastics with colorful exercise mat, children style, white background",true);
        f("VOLI","olahraga","🏐",
                "Voli! Bola voli dimainkan dengan memukul bola melewati net. Dimainkan dua tim bersama-sama!",
                "cute cartoon volleyball players at net, white blue ball, beach volleyball, children style, white background",true);
        f("TENIS","olahraga","🎾",
                "Tenis! Tenis dimainkan di lapangan dengan raket dan bola berbulu. Smash! Seru sekali bermain tenis!",
                "cute cartoon child holding tennis racket with green court background, children style, white background",true);
        f("GOLF","olahraga","⛳",
                "Golf! Golf dimainkan dengan memukul bola ke dalam lubang. Lapangan golf sangat hijau dan luas.",
                "cute cartoon child playing golf on green course, golf club and ball, children style, white background",true);
        f("TINJU","olahraga","🥊",
                "Tinju! Tinju adalah olahraga bela diri yang butuh kekuatan dan teknik. Pakai sarung tangan merah!",
                "cute cartoon child boxing with red gloves and punching bag, children style, white background",true);

        // ══════════════════════════════════════════
        //  15. ANGKA (10 kata) — BARU
        // ══════════════════════════════════════════
        f("SATU","angka","1️⃣",
                "Satu! Satu adalah angka pertama. Satu jari, satu mata, satu hidung. Ayo hitung bersama! Satu!",
                "cute cartoon number 1 with eyes and smile, colorful bold, children learning style, white background",true);
        f("DUA","angka","2️⃣",
                "Dua! Dua artinya sepasang. Kita punya dua tangan, dua kaki, dua telinga. Dua itu simetris!",
                "cute cartoon number 2 with eyes and smile, colorful bold, children learning style, white background",true);
        f("TIGA","angka","3️⃣",
                "Tiga! Tiga adalah angka setelah dua. Tiga sisi segitiga. Tiga warna lampu lalu lintas!",
                "cute cartoon number 3 with eyes and smile, colorful bold, children learning style, white background",true);
        f("EMPAT","angka","4️⃣",
                "Empat! Empat kaki kucing, empat roda mobil, empat sudut persegi. Hitung sampai empat!",
                "cute cartoon number 4 with eyes and smile, colorful bold, children learning style, white background",true);
        f("LIMA","angka","5️⃣",
                "Lima! Lima jari di satu tangan. Lima hari sekolah dalam seminggu. Tos lima! High five!",
                "cute cartoon number 5 with eyes and smile giving high five, colorful, children learning style, white background",true);
        f("ENAM","angka","6️⃣",
                "Enam! Lebah punya enam kaki. Serangga punya enam kaki. Enam sisi kubus. Hitung sampai enam!",
                "cute cartoon number 6 with eyes and smile, colorful bold, children learning style, white background",true);
        f("TUJUH","angka","7️⃣",
                "Tujuh! Tujuh hari dalam seminggu. Tujuh warna pelangi. Tujuh adalah angka keberuntungan!",
                "cute cartoon number 7 with rainbow behind it, children learning style, colorful, white background",true);
        f("DELAPAN","angka","8️⃣",
                "Delapan! Laba-laba punya delapan kaki. Gurita punya delapan tentakel. Delapan seperti tidur!",
                "cute cartoon number 8 with spider web design, friendly, children learning style, white background",true);
        f("SEMBILAN","angka","9️⃣",
                "Sembilan! Sembilan adalah angka terbesar sebelum sepuluh. Kucing punya sembilan nyawa!",
                "cute cartoon number 9 with eyes and smile, colorful bold, children learning style, white background",true);
        f("SEPULUH","angka","🔟",
                "Sepuluh! Sepuluh jari di dua tangan. Sepuluh angka dari nol sampai sembilan. Hore selesai!",
                "cute cartoon number 10 with confetti celebration, both hands showing 10 fingers, children style, white background",true);

        // ══════════════════════════════════════════
        //  16. CUACA (8 kata) — BARU
        // ══════════════════════════════════════════
        f("CERAH","cuaca","☀️",
                "Cerah! Cuaca cerah artinya matahari bersinar terang tanpa awan. Hari cerah cocok bermain di luar!",
                "cute cartoon sunny clear sky with smiling sun and few white clouds, children style, white background",true);
        f("MENDUNG","cuaca","☁️",
                "Mendung! Cuaca mendung artinya langit penuh awan gelap. Biasanya mendung pertanda akan hujan.",
                "cute cartoon dark gray clouds covering sky, child looking up worriedly, children style, white background",true);
        f("HUJAN LEBAT","cuaca","⛈️",
                "Hujan lebat! Hujan lebat turun dengan sangat deras disertai petir. Di dalam rumah lebih aman!",
                "cute cartoon heavy rain with lightning and thunder, child safely inside watching, children style, white background",true);
        f("ANGIN","cuaca","💨",
                "Angin! Angin adalah udara yang bergerak. Angin sepoi-sepoi membuat sejuk. Angin bisa menerbangkan layang-layang!",
                "cute cartoon wind blowing leaves and kite flying, friendly wind face, children style, white background",true);
        f("SALJU","cuaca","❄️",
                "Salju! Salju adalah air beku yang turun dari langit seperti kapas putih. Bisa dibuat bola salju!",
                "cute cartoon snowflakes falling, child in winter clothes catching snowflakes, children style, white background",true);
        f("PANAS","cuaca","🌡️",
                "Panas! Cuaca panas membuat kita berkeringat. Saat panas, minum air yang banyak ya!",
                "cute cartoon thermometer showing high temperature, sun shining intensely, children style, white background",true);
        f("DINGIN","cuaca","🧊",
                "Dingin! Cuaca dingin membuat kita menggigil. Pakai jaket tebal saat cuaca dingin ya!",
                "cute cartoon child wrapped in warm scarf and coat shivering, snowflakes, children style, white background",true);
        f("KABUT","cuaca","🌫️",
                "Kabut! Kabut adalah uap air tebal di udara yang membuat pandangan tidak jelas. Biasanya pagi hari.",
                "cute cartoon foggy morning with barely visible trees and houses, children style, white background",true);


    }


    // ── Public API ────────────────────────────────────────────

    public KnowledgeFrame getFrame(String word) {
        if (word == null) return null;
        return frames.get(word.toUpperCase().trim());
    }

    public boolean hasFrame(String word) {
        return word != null && frames.containsKey(word.toUpperCase().trim());
    }

    public Collection<KnowledgeFrame> getAll() {
        return Collections.unmodifiableCollection(frames.values());
    }

    public Set<String> getCategories() {
        Set<String> cats = new TreeSet<>();
        for (KnowledgeFrame f : frames.values()) cats.add(f.category);
        return cats;
    }

    public List<KnowledgeFrame> getByCategory(String category) {
        List<KnowledgeFrame> result = new ArrayList<>();
        for (KnowledgeFrame f : frames.values())
            if (category.equalsIgnoreCase(f.category)) result.add(f);
        result.sort(Comparator.comparing(f -> f.word));
        return result;
    }

    public List<KnowledgeFrame> search(String query) {
        if (query == null || query.isBlank()) return new ArrayList<>(frames.values());
        String q = query.toUpperCase().trim();
        List<KnowledgeFrame> result = new ArrayList<>();
        for (KnowledgeFrame f : frames.values())
            if (f.word.contains(q) || f.category.toUpperCase().contains(q)) result.add(f);
        return result;
    }

    public int getTotalWords() { return frames.size(); }
}
