package com.example.data.repository

import com.example.data.model.WordLevel

object LevelRepository {

    private data class WordTemplate(
        val word: String,
        val category: String,
        val emoji: String,
        val pronunciation: String,
        val meaning: String,
        val simpleExplanation: String,
        val exampleSentence: String,
        val hint: String
    )

    private val base100Levels: List<WordLevel> = listOf(
        // Levels 1 - 10: Very Easy 3-Letter Words
        WordLevel(1, 1, "CAT", "Animals", "🐱", "kæt", "A small furry pet with whiskers", "Cats love to sleep and play with yarn.", "The cat sleeps on the mat.", "A friendly pet that meows", 1),
        WordLevel(2, 2, "DOG", "Animals", "🐶", "dɔːɡ", "A loyal pet that barks", "Dogs are faithful animal friends.", "The dog wags its tail.", "A friendly pet that barks", 1),
        WordLevel(3, 3, "SUN", "Nature", "☀️", "sʌn", "The bright star in the daytime sky", "The sun gives us light and warmth.", "The sun is shining brightly today.", "Bright star in the sky", 1),
        WordLevel(4, 4, "BAT", "Animals", "🦇", "bæt", "A flying mammal that comes out at night", "Bats can fly in the dark using sound.", "A bat flew high in the night sky.", "A nocturnal flying animal", 1),
        WordLevel(5, 5, "CUP", "Objects", "☕", "kʌp", "A small container used for drinking", "We drink warm cocoa from a cup.", "She filled the cup with water.", "Used for drinking juice or tea", 1),
        WordLevel(6, 6, "PEN", "School", "🖊️", "pen", "A tool used for writing with ink", "Use a pen to draw or write notes.", "He wrote his name with a blue pen.", "Used to write on paper", 1),
        WordLevel(7, 7, "BUS", "Vehicles", "🚌", "bʌs", "A large vehicle that carries many people", "A bus takes children to school.", "The big yellow bus arrived on time.", "Large vehicle with many seats", 1),
        WordLevel(8, 8, "HAT", "Clothing", "🎩", "hæt", "A covering worn on top of your head", "A hat keeps your head warm or shaded.", "He put on a colorful sun hat.", "Worn on your head", 1),
        WordLevel(9, 9, "MAP", "Adventure", "🗺️", "mæp", "A drawing showing places and roads", "A map helps us find where to go.", "We looked at the map to find the treasure.", "Shows directions to places", 1),
        WordLevel(10, 10, "RUN", "Actions", "🏃", "rʌn", "To move fast with your feet", "Running keeps your body healthy and strong.", "The children run fast in the playground.", "To move fast on foot", 1),

        // Levels 11 - 20: Easy 3-Letter Words
        WordLevel(11, 11, "PIG", "Animals", "🐷", "pɪɡ", "A pink farm animal that oinks", "Pigs love playing in mud to keep cool.", "The little pig oinked happily.", "Pink farm animal that says oink", 2),
        WordLevel(12, 12, "ANT", "Insects", "🐜", "ænt", "A tiny hardworking insect", "Ants build big underground tunnels.", "The tiny ant carried a big leaf.", "Tiny insect that works in teams", 2),
        WordLevel(13, 13, "FOX", "Animals", "🦊", "fɑːks", "A clever wild animal with a bushy tail", "Foxes are known for being clever.", "The red fox ran through the forest.", "Clever wild animal with a red coat", 2),
        WordLevel(14, 14, "BOX", "Objects", "📦", "bɑːks", "A square container with flat sides", "Put toys inside the cardboard box.", "She opened the surprise gift box.", "Used to hold toys or items", 2),
        WordLevel(15, 15, "BED", "Home", "🛏️", "bed", "Furniture used for sleeping comfortably", "Sleep well in your cozy bed at night.", "He jumped into bed for a bedtime story.", "Where you sleep at night", 2),
        WordLevel(16, 16, "COW", "Animals", "🐮", "kaʊ", "A large farm animal that gives milk", "Cows eat green grass in the field.", "The spotted cow says moo.", "Gives us fresh milk", 2),
        WordLevel(17, 17, "OWL", "Birds", "🦉", "aʊl", "A wise bird that turns its head far around", "Owls stay awake during the night.", "The owl hooted softly in the tree.", "Bird that hoots at night", 2),
        WordLevel(18, 18, "BOY", "People", "👦", "bɔɪ", "A young male child", "The boy built a castle out of blocks.", "The boy smiled and waved hello.", "A young male child", 2),
        WordLevel(19, 19, "SKY", "Nature", "🌌", "skaɪ", "The space above the Earth with clouds", "Clouds float peacefully in the blue sky.", "Look up at the clear blue sky.", "Space above us with clouds", 2),
        WordLevel(20, 20, "BUG", "Insects", "🐞", "bʌɡ", "A small creeping or flying insect", "Bugs crawl among flowers and leaves.", "A tiny ladybug landed on her thumb.", "Small crawling insect", 2),

        // Levels 21 - 30: Common 4-Letter Words
        WordLevel(21, 21, "DUCK", "Animals", "🦆", "dʌk", "A water bird that quacks and swims", "Ducks have webbed feet for swimming.", "The yellow duck quacked in the pond.", "Water bird that quacks", 3),
        WordLevel(22, 22, "FISH", "Animals", "🐟", "fɪʃ", "A creature that lives and swims in water", "Fish use fins to glide underwater.", "A bright orange fish swam past the rocks.", "Swims underwater with fins", 3),
        WordLevel(23, 23, "FROG", "Animals", "🐸", "frɑːɡ", "A green hopping amphibian", "Frogs hop high and catch flies with long tongues.", "The green frog leaped onto a lily pad.", "Green hopping water animal", 3),
        WordLevel(24, 24, "LION", "Animals", "🦁", "laɪən", "A big wild cat called the King of the Jungle", "Lions have majestic golden manes.", "The brave lion roared across the savanna.", "King of the jungle cat", 3),
        WordLevel(25, 25, "MOON", "Space", "🌙", "muːn", "The natural light in the night sky", "The moon glows brightly above Earth.", "A crescent moon shone in the dark sky.", "Glows in the sky at night", 3),
        WordLevel(26, 26, "STAR", "Space", "⭐", "stɑːr", "A glowing light distant in outer space", "Stars twinkle like diamonds in the night.", "Make a wish on a shooting star.", "Twinkling light in the night sky", 3),
        WordLevel(27, 27, "TREE", "Nature", "🌳", "triː", "A tall plant with trunk and leaves", "Trees give us fresh shade and oxygen.", "Birds built a nest in the tall green tree.", "Tall plant with green leaves", 3),
        WordLevel(28, 28, "BOOK", "School", "📚", "bʊk", "Pages bound together containing stories", "Reading books unlocks fun adventures.", "She read a wonderful fairytale book.", "Contains written stories and pictures", 3),
        WordLevel(29, 29, "BALL", "Sports", "⚽", "bɔːl", "A round object used in games", "Bounce the ball or kick it to a friend.", "He kicked the soccer ball into the goal.", "Round object used for playing games", 3),
        WordLevel(30, 30, "MILK", "Food", "🥛", "mɪlk", "A nutritious white drink from cows", "Milk helps build strong bones and teeth.", "He drank a cold glass of fresh milk.", "White healthy drink", 3),

        // Levels 31 - 40: Medium 4-Letter Vocabulary
        WordLevel(31, 31, "CAKE", "Food", "🎂", "keɪk", "A sweet baked birthday dessert", "Decorate the cake with sweet frosting.", "They blew out candles on the birthday cake.", "Sweet dessert with candles", 3),
        WordLevel(32, 32, "ROSE", "Nature", "🌹", "roʊz", "A fragrant flower with sweet smell", "Roses bloom in lovely gardens.", "He picked a fragrant red rose.", "Sweet smelling garden flower", 3),
        WordLevel(33, 33, "BIRD", "Animals", "🐦", "bɜːrd", "A feathered animal that can fly and sing", "Birds sing happy songs in the morning.", "A blue bird chirped in the treetop.", "Feathered creature that flies", 3),
        WordLevel(34, 34, "BEAR", "Animals", "🐻", "ber", "A large furry wild mammal", "Bears love eating sweet honey and fish.", "A fluffy brown bear rested in its cave.", "Large furry forest animal", 3),
        WordLevel(35, 35, "BOAT", "Vehicles", "⛵", "boʊt", "A small vessel for travelling on water", "Sail a boat across calm blue water.", "The sailboat glided over the lake.", "Floats on lake or ocean water", 3),
        WordLevel(36, 36, "DOLL", "Toys", "🪆", "dɑːl", "A toy shaped like a small person", "She dressed up her favorite doll.", "The girl hugged her plush toy doll.", "Toy human figure", 3),
        WordLevel(37, 37, "SNOW", "Weather", "❄️", "snoʊ", "Soft white frozen water crystals", "Catch falling snow on your tongue.", "Children built a snowman in the white snow.", "Soft white winter ice flakes", 3),
        WordLevel(38, 38, "WIND", "Weather", "💨", "wɪnd", "Moving air that blows gently or strongly", "Wind turns giant wind turbines.", "The cool wind blew the colorful kite high.", "Moving air outside", 3),
        WordLevel(39, 39, "RAIN", "Weather", "🌧️", "reɪn", "Water drops falling from clouds", "Rain helps green plants grow tall.", "Raindrops pattered softly on the window.", "Water drops falling from clouds", 3),
        WordLevel(40, 40, "SHIP", "Vehicles", "🚢", "ʃɪp", "A large vessel for long ocean voyages", "Ships carry ocean cargo and travelers.", "The giant cruise ship sailed into harbor.", "Large vessel on the sea", 3),

        // Levels 41 - 50: Mixed 5-Letter Words (Part 1)
        WordLevel(41, 41, "APPLE", "Food", "🍎", "ˈæp.əl", "A crunchy sweet round fruit", "Apples grow on sunny orchard trees.", "She took a bite of a sweet red apple.", "Red or green crunchy fruit", 4),
        WordLevel(42, 42, "BREAD", "Food", "🍞", "bred", "A baked food made from flour", "Toast fresh bread for delicious breakfast.", "The baker made warm fresh bread.", "Baked loaf made from flour", 4),
        WordLevel(43, 43, "BEACH", "Nature", "🏖️", "biːtʃ", "Sandy shore beside the ocean waves", "Build sandcastles along the ocean beach.", "We collected pretty shells at the beach.", "Sandy ocean shore", 4),
        WordLevel(44, 44, "CLOUD", "Weather", "☁️", "klaʊd", "Fluffy white vapor floating in the sky", "Clouds look like fluffy white cotton.", "A fluffy white cloud drifted past.", "Fluffy white sky formation", 4),
        WordLevel(45, 45, "DANCE", "Actions", "💃", "dæns", "Moving your body to rhythmic music", "Dancing brings joy and smiles to everyone.", "They danced together to happy music.", "To move rhythmically to music", 4),
        WordLevel(46, 46, "GREEN", "Colors", "🟢", "ɡriːn", "The color of fresh grass and leaves", "Green is the color of nature.", "The frog jumped into the green grass.", "Color of grass and trees", 4),
        WordLevel(47, 47, "HAPPY", "Feelings", "😊", "ˈhæp.i", "Feeling cheerful, joyful and bright", "A big smile shows you feel happy.", "The children were happy on the field trip.", "Feeling joyful and smiling", 4),
        WordLevel(48, 48, "HEART", "Body", "❤️", "hɑːrt", "The muscle pumping blood in your body", "A heart symbol stands for love and care.", "Her heart beat steadily as she ran.", "Organ pumping blood; symbol of love", 4),
        WordLevel(49, 49, "SMILE", "Feelings", "😄", "smaɪl", "A bright happy expression on your face", "A warm smile brightens someone's day.", "He gave a friendly smile to his friend.", "Happy facial expression", 4),
        WordLevel(50, 50, "CANDY", "Food", "🍬", "ˈkæn.di", "Sweet confectionery treat", "Enjoy sweet candy treats in moderation.", "She shared colorful fruit candy with us.", "Sweet sugar treat", 4),

        // Levels 51 - 60: Common 5-Letter Words (Part 2)
        WordLevel(51, 51, "HOUSE", "Home", "🏠", "haʊs", "A building where people live", "Our house has a cozy living room.", "They painted their house a warm yellow.", "A building where families live", 4),
        WordLevel(52, 52, "MOUSE", "Animals", "🐭", "maʊs", "A tiny quick rodent with a long tail", "The little mouse nibbled on yellow cheese.", "A quiet mouse scuttled behind the door.", "Tiny rodent that nibbles cheese", 4),
        WordLevel(53, 53, "WATER", "Nature", "💧", "ˈwɔː.tər", "Clear liquid essential for life", "Drink plenty of clean water daily.", "Cool water refreshed the thirsty runner.", "Clear drink essential for life", 4),
        WordLevel(54, 54, "TRAIN", "Vehicles", "🚂", "treɪn", "A connected line of railway cars", "Trains chug along long steel tracks.", "The steam train blew its whistle high.", "Choo-choo vehicle on tracks", 4),
        WordLevel(55, 55, "TIGER", "Animals", "🐯", "ˈtaɪ.ɡər", "A big wild cat with dark orange stripes", "Tigers are fast and powerful hunters.", "The striped tiger walked softly in the jungle.", "Big wild cat with dark stripes", 4),
        WordLevel(56, 56, "ZEBRA", "Animals", "🦓", "ˈziː.brə", "An African wild horse with black and white stripes", "Every zebra has a unique pattern of stripes.", "A zebra grazed peacefully on the plain.", "Wild horse with black and white stripes", 4),
        WordLevel(57, 57, "SHARK", "Animals", "🦈", "ʃɑːrk", "A fast swimming ocean predator", "Sharks have sharp teeth and keen sense.", "The great shark swam smoothly underwater.", "Fast ocean swimmer with sharp teeth", 4),
        WordLevel(58, 58, "GRAPE", "Food", "🍇", "ɡreɪp", "A small juicy round fruit growing in bunches", "Grapes grow in clusters on climbing vines.", "Sweet purple grapes make delicious snacks.", "Small juicy fruit in bunches", 4),
        WordLevel(59, 59, "CHAIR", "Home", "🪑", "tʃer", "A seat with a back for one person", "Sit comfortably in a sturdy chair.", "She pulled up a chair to read her book.", "Furniture for sitting on", 4),
        WordLevel(60, 60, "CLOCK", "Objects", "⏰", "klɑːk", "A device showing hours and minutes", "The ticking clock tells us the time.", "The alarm clock rang at seven morning.", "Tells the time of day", 4),

        // Levels 61 - 70: Medium 5-Letter Vocabulary (Part 3)
        WordLevel(61, 61, "FLOWER", "Nature", "🌸", "ˈflaʊ.ər", "The colorful blooming part of a plant", "Bees land on flowers to gather sweet nectar.", "A bright yellow flower bloomed in spring.", "Blossom of a plant", 4),
        WordLevel(62, 62, "LIGHT", "Science", "💡", "laɪt", "Energy that lets our eyes see things", "Turn on the light when it gets dark.", "Sunlight filled the room with brightness.", "Helps us see in the dark", 4),
        WordLevel(63, 63, "MAGIC", "Fantasy", "🪄", "ˈmædʒ.ɪk", "Mysterious power in wonder stories", "Magicians perform amazing magic tricks.", "The fairy waved her magic wand.", "Wonderful fairy power", 4),
        WordLevel(64, 64, "MUSIC", "Arts", "🎵", "ˈmjuː.zɪk", "Pleasant sounds produced by instruments or voices", "Listen to happy music and sing along.", "The cheerful music made us want to jump.", "Melodies played on instruments", 4),
        WordLevel(65, 65, "OCEAN", "Nature", "🌊", "ˈoʊ.ʃən", "Vast body of salt water covering Earth", "Whales and dolphins live in the deep ocean.", "Waves crashed gently on the ocean beach.", "Vast salt water sea", 4),
        WordLevel(66, 66, "PAPER", "School", "📄", "ˈpeɪ.pər", "Thin material for writing and drawing", "Draw colorful pictures on clean paper.", "He folded paper into a flying airplane.", "Used for writing or drawing", 4),
        WordLevel(67, 67, "PLANET", "Space", "🪐", "ˈplæn.ɪt", "A celestial body orbiting a star like our Earth", "Earth is the blue planet we call home.", "Saturn is a planet surrounded by rings.", "Celestial world orbiting a star", 4),
        WordLevel(68, 68, "ROBOT", "Science", "🤖", "ˈroʊ.bɑːt", "A machine programmed to perform tasks", "Robots can perform tricky jobs automatically.", "The friendly robot beeped and waved.", "Smart automated machine", 4),
        WordLevel(69, 69, "SPACE", "Space", "🚀", "speɪs", "The infinite realm beyond Earth's atmosphere", "Astronauts travel into outer space.", "Stars and galaxies fill outer space.", "The cosmos beyond Earth", 4),
        WordLevel(70, 70, "WORLD", "Geography", "🌍", "wɜːrld", "The planet Earth with all its life", "Explore different cultures around the world.", "There are many wonderful places in the world.", "Our planet Earth", 4),

        // Levels 71 - 80: 6-Letter Educational Words
        WordLevel(71, 71, "ANIMAL", "Nature", "🐾", "ˈæn.ɪ.məl", "A living creature like dog, cat or bear", "Be kind to every living animal.", "We saw many wild animals at the zoo.", "Living creature like dog or lion", 5),
        WordLevel(72, 72, "BANANA", "Food", "🍌", "bəˈnæn.ə", "A long yellow sweet fruit enjoyed by monkeys", "Bananas are packed with healthy energy.", "He peeled a sweet yellow banana.", "Yellow curved sweet fruit", 5),
        WordLevel(73, 73, "MONKEY", "Animals", "🐒", "ˈmʌŋ.ki", "A clever tree-climbing mammal", "Monkeys swing gracefully through tree branches.", "The cheeky monkey swung by its tail.", "Tree-climbing mammal that swings", 5),
        WordLevel(74, 74, "RABBIT", "Animals", "🐰", "ˈræb.ɪt", "A gentle hopping mammal with long ears", "Rabbits twitch their noses and munch carrots.", "The fluffy white rabbit hopped across the lawn.", "Hopping furry pet with long ears", 5),
        WordLevel(75, 75, "ROCKET", "Space", "🚀", "ˈrɑː.kɪt", "A vehicle designed to fly into outer space", "Rockets blast off with powerful fiery engines.", "The space rocket launched toward the moon.", "Vehicle blasting into space", 5),
        WordLevel(76, 76, "SUNNY", "Weather", "🌤️", "ˈsʌn.i", "Bright with warm rays from the sun", "Sunny weather is perfect for outdoor games.", "It was a warm and sunny afternoon.", "Bright and filled with sunlight", 5),
        WordLevel(77, 77, "CASTLE", "Fantasy", "🏰", "ˈkæs.əl", "A majestic fortified stone building", "Knights and queens live in fairy tale castles.", "A grand stone castle stood atop the hill.", "Majestic stone building with towers", 5),
        WordLevel(78, 78, "DRAGON", "Fantasy", "🐉", "ˈdræɡ.ən", "A mythical creature that breathes fire", "Dragons guard secret treasures in legends.", "A friendly green dragon flew through clouds.", "Mythical creature that breathes fire", 5),
        WordLevel(79, 79, "FOREST", "Nature", "🌲", "ˈfɔːr.ɪst", "A vast area dense with tall trees", "Birds and deer live peacefully in the forest.", "We walked along the leafy forest trail.", "Large area covered with trees", 5),
        WordLevel(80, 80, "ISLAND", "Geography", "🏝️", "ˈaɪ.lənd", "Land completely surrounded by ocean water", "Palms sway gently on tropical islands.", "The treasure map showed a small island.", "Land surrounded by water", 5),

        // Levels 81 - 90: Advanced 6-Letter Words
        WordLevel(81, 81, "GUITAR", "Arts", "🎸", "ɡɪˈtɑːr", "A stringed instrument played by strumming", "Strumming guitar strings creates music.", "She played a catchy tune on her guitar.", "Stringed music instrument", 5),
        WordLevel(82, 82, "PIRATE", "Adventure", "🏴‍☠️", "ˈpaɪ.rət", "A sea explorer seeking hidden treasure", "Pirates sail high seas looking for chest gold.", "The brave pirate searched for the island map.", "Sea explorer seeking treasure", 5),
        WordLevel(83, 83, "PRINCE", "Fantasy", "🤴", "prɪns", "A royal son of a king and queen", "The prince rode a white horse to the castle.", "A prince smiled and greeted the kingdom.", "Royal son of king and queen", 5),
        WordLevel(84, 84, "SPRING", "Seasons", "🌱", "sprɪŋ", "The season when flowers bloom and trees bud", "In spring, nature wakes up with warm sunshine.", "Fresh green leaves grow everywhere in spring.", "Season when flowers bloom", 5),
        WordLevel(85, 85, "SUMMER", "Seasons", "☀️", "ˈsʌm.ər", "The warmest season of the year", "Summer brings long sunny days and beach fun.", "We ate cold ice cream on a warm summer day.", "Warmest season of the year", 5),
        WordLevel(86, 86, "WINTER", "Seasons", "❄️", "ˈwɪn.tər", "The cold season with ice and falling snow", "Bundle up warmly in cozy winter coats.", "Snowflakes covered the ground in winter.", "Coldest season with snow", 5),
        WordLevel(87, 87, "YELLOW", "Colors", "🟡", "ˈjel.oʊ", "The bright warm color of lemons and sun", "Yellow is a bright happy color.", "She wore a bright yellow raincoat.", "Color of sunflowers and lemons", 5),
        WordLevel(88, 88, "WINDOW", "Home", "🪟", "ˈwɪn.doʊ", "A glass frame in a wall letting light inside", "Look through the window to watch the rain.", "Sunlight streamed through the clean window.", "Glass opening in a wall", 5),
        WordLevel(89, 89, "GARDEN", "Nature", "🏡", "ˈɡɑːr.dən", "A plot of land with growing flowers and plants", "Tend the garden with water and sunshine.", "Red tomatoes and roses bloomed in the garden.", "Plot of growing flowers and vegetables", 5),
        WordLevel(90, 90, "SCHOOL", "Education", "🏫", "skuːl", "A place where children learn together", "In school we discover reading, science and math.", "Friends met outside the school building.", "Place of learning and reading", 5),

        // Levels 91 - 100: 7-8 Letter Educational Vocabulary (Master Tier)
        WordLevel(91, 91, "RAINBOW", "Weather", "🌈", "ˈreɪn.boʊ", "A beautiful arc of colors in the sky after rain", "A rainbow has red, orange, yellow, green, blue and violet.", "A colorful rainbow appeared across the blue sky.", "Multi-colored arc in the sky after rain", 5),
        WordLevel(92, 92, "DOLPHIN", "Animals", "🐬", "ˈdɑːl.fɪn", "An intelligent playful sea mammal", "Dolphins leap gracefully above ocean waves.", "A friendly dolphin swam alongside our boat.", "Smart ocean mammal that leaps", 5),
        WordLevel(93, 93, "ELEPHANT", "Animals", "🐘", "ˈel.ə.fənt", "A giant mammal with a long trunk and big ears", "Elephants use their long trunks to drink and spray water.", "The gentle elephant trumpeted happily.", "Giant land animal with a long trunk", 5),
        WordLevel(94, 94, "DIAMOND", "Gems", "💎", "ˈdaɪ.mənd", "A sparkling precious gem stone", "Diamonds sparkle with brilliant light.", "A shining diamond glittered in the light.", "Sparkling precious stone", 5),
        WordLevel(95, 95, "KINGDOM", "Fantasy", "🏰", "ˈkɪŋ.dəm", "A realm ruled by a benevolent king and queen", "Peace and happiness filled the fairytale kingdom.", "Travelers visited the prosperous mountain kingdom.", "Land ruled by king and queen", 5),
        WordLevel(96, 96, "UNICORN", "Fantasy", "🦄", "ˈjuː.nɪ.kɔːrn", "A magical horse with a single spiraled horn", "Unicorns appear in enchanting fairy stories.", "The white unicorn trotted through the magical wood.", "Magical horned horse", 5),
        WordLevel(97, 97, "MOUNTAIN", "Geography", "⛰️", "ˈmaʊn.tən", "A huge high landform reaching into clouds", "Snow covers peak tops of tall mountains.", "Climbers hiked up the scenic mountain path.", "Very high rocky landform", 5),
        WordLevel(98, 98, "BUTTERFLY", "Insects", "🦋", "ˈbʌt.ər.flaɪ", "A beautiful insect with colorful wings", "Butterflies flutter gently among blooming flowers.", "A bright orange butterfly landed on a rose.", "Insect with colorful fluttering wings", 5),
        WordLevel(99, 99, "EXPLORER", "Adventure", "🧭", "ˈek.splɔːr.ər", "A person who travels to discover new places", "Explorers discover uncharted lands and paths.", "The brave explorer charted a new river route.", "Person who discovers new places", 5),
        WordLevel(100, 100, "ADVENTURE", "Adventure", "🗺️", "ədˈven.tʃər", "An exciting trip or fun journey of discovery", "Learning new words is an exciting adventure!", "Welcome to Word Adventure, your spelling journey!", "Exciting journey of discovery", 5)
    )

    private val extendedWordBank: List<WordTemplate> = listOf(
        WordTemplate("GIRAFFE", "Animals", "🦒", "dʒəˈræf", "A tall African animal with a very long neck", "Giraffes reach high tree leaves easily.", "The tall giraffe nibbled leaves from treetops.", "Tall mammal with a long neck"),
        WordTemplate("PANDA", "Animals", "🐼", "ˈpæn.də", "A black and white bear that eats bamboo", "Pandas love munching green bamboo shoots.", "A cute panda rested in the bamboo grove.", "Black and white bamboo bear"),
        WordTemplate("KOALA", "Animals", "🐨", "koʊˈɑː.lə", "An Australian tree animal that sleeps a lot", "Koalas live in eucalyptus tree branches.", "The cozy koala hugged the tree trunk.", "Tree-dwelling Australian marsupial"),
        WordTemplate("WHALE", "Animals", "🐋", "weɪl", "The largest sea creature living in oceans", "Whales spout water high in the air.", "A blue whale swam gracefully through blue waters.", "Giant ocean swimmer"),
        WordTemplate("OCTOPUS", "Animals", "🐙", "ˈɑːk.tə.pəs", "A clever sea animal with eight arms", "Octopuses can change color to hide.", "The octopus wiggled its eight tentacles.", "Eight-armed clever sea creature"),
        WordTemplate("PENGUIN", "Animals", "🐧", "ˈpeŋ.ɡwɪn", "A flightless bird that swims in icy seas", "Penguins waddle gracefully on snowy ice.", "A penguin slid down the icy hill.", "Waddling Antarctic sea bird"),
        WordTemplate("FLAMINGO", "Animals", "🦩", "fləˈmɪŋ.ɡoʊ", "A tall pink bird that stands on one leg", "Flamingos get pink feathers from eating shrimp.", "A pink flamingo stood gracefully in water.", "Tall pink wading bird"),
        WordTemplate("PEACOCK", "Animals", "🦚", "ˈpiː.kɑːk", "A bird with colorful fan-shaped tail feathers", "Peacocks display dazzling blue and green feathers.", "The peacock fanned its bright tail.", "Bird with colorful fan feathers"),
        WordTemplate("EAGLE", "Animals", "🦅", "ˈiː.ɡəl", "A powerful flying bird with sharp eyesight", "Eagles soar high in the open sky.", "A majestic eagle glided above mountains.", "High-flying bird of prey"),
        WordTemplate("TURTLE", "Animals", "🐢", "ˈtɜːr.təl", "A slow animal protected by a hard shell", "Turtles carry their cozy homes on their backs.", "A tiny sea turtle crawled to the water.", "Reptile with a hard protective shell"),
        WordTemplate("ORANGE", "Food", "🍊", "ˈɔːr.ɪndʒ", "A juicy sweet citrus fruit", "Oranges are packed with healthy Vitamin C.", "He drank fresh sweet orange juice.", "Round orange citrus fruit"),
        WordTemplate("CHERRY", "Food", "🍒", "ˈtʃer.i", "A small red sweet stone fruit", "Cherries taste delicious in summer pies.", "She picked two red cherries from the branch.", "Small red fruit on a stem"),
        WordTemplate("MANGO", "Food", "🥭", "ˈmæŋ.ɡoʊ", "A sweet tropical yellow-orange fruit", "Mangos are soft and deliciously sweet.", "We enjoyed slices of ripe mango.", "Sweet tropical yellow fruit"),
        WordTemplate("PEACH", "Food", "🍑", "piːtʃ", "A fuzzy soft sweet summer fruit", "Peaches smell fragrant and taste sweet.", "She took a bite of a juicy ripe peach.", "Fuzzy sweet pinkish fruit"),
        WordTemplate("PINEAPPLE", "Food", "🍍", "ˈpaɪn.æp.əl", "A spiky tropical fruit with sweet yellow flesh", "Pineapples grow in sunny warm climates.", "We cut fresh pineapple into sweet slices.", "Spiky tropical sweet fruit"),
        WordTemplate("STRAWBERRY", "Food", "🍓", "ˈstrɔː.ber.i", "A sweet red berry with tiny seeds", "Strawberries make tasty ice cream flavors.", "She ate a bowl of sweet red strawberries.", "Red juicy seed-covered berry"),
        WordTemplate("WATERMELON", "Food", "🍉", "ˈwɔː.tər.mel.ən", "A huge green melon with sweet red juice", "Watermelon is refreshing on hot summer days.", "We ate cold watermelon at the picnic.", "Huge green juicy melon"),
        WordTemplate("BROCCOLI", "Food", "🥦", "ˈbrɑː.kə.li", "A healthy green vegetable looking like a tiny tree", "Broccoli gives your body strong energy.", "She ate crunch green broccoli florets.", "Green tree-shaped vegetable"),
        WordTemplate("CARROT", "Food", "🥕", "ˈkær.ət", "An orange crunchy root vegetable", "Carrots help keep your vision healthy.", "The bunny crunched a fresh orange carrot.", "Crunchy orange root vegetable"),
        WordTemplate("COOKIE", "Food", "🍪", "ˈkʊk.i", "A sweet baked snack with chocolate chips", "Warm cookies go great with a glass of milk.", "Baking fresh chocolate chip cookies smells delicious.", "Sweet baked snack"),
        WordTemplate("PANCAKE", "Food", "🥞", "ˈpæn.keɪk", "A flat round breakfast cake with syrup", "Stack warm pancakes high with maple syrup.", "We made fluffy blueberry pancakes.", "Flat round breakfast cake"),
        WordTemplate("VOLCANO", "Nature", "🌋", "vɑːlˈkeɪ.noʊ", "A mountain that can erupt with fiery lava", "Volcanoes form rich soil over time.", "Smoke rose softly from the sleeping volcano.", "Fiery mountain with lava"),
        WordTemplate("WATERFALL", "Nature", "🌊", "ˈwɔː.tər.fɔːl", "Cascading water tumbling down rocks", "Waterfalls spray cool mist in green glades.", "A rushing waterfall tumbled into the river.", "Water falling down steep rocks"),
        WordTemplate("RAINFOREST", "Nature", "🌴", "ˈreɪn.fɔːr.ɪst", "A dense green jungle with heavy rainfall", "Rainforests house thousands of colorful creatures.", "Monkeys calls echoed through the tropical rainforest.", "Dense lush tropical forest"),
        WordTemplate("DESERT", "Geography", "🏜️", "ˈdez.ərt", "A dry sandy region with hot sunshine", "Camels travel easily across warm desert dunes.", "The golden desert stretched to the horizon.", "Dry sandy land with cactus"),
        WordTemplate("SUNFLOWER", "Nature", "🌻", "ˈsʌn.flaʊ.ər", "A tall yellow blossom following the sun", "Sunflowers grow tall toward the sky.", "Bright yellow sunflowers bloomed in fields.", "Tall yellow sun-facing flower"),
        WordTemplate("GALAXY", "Space", "🌌", "ˈɡæl.ək.si", "A massive system of billions of stars", "Our galaxy is named the Milky Way.", "Telescopes reveal distant swirling galaxies.", "Swirling cluster of stars"),
        WordTemplate("COMET", "Space", "☄️", "ˈkɑː.mɪt", "A icy space rock with a glowing tail", "Comets streak past far away stars.", "A shining comet trailed light across night.", "Icy space rock with glowing tail"),
        WordTemplate("ASTRONAUT", "Space", "👨‍🚀", "ˈæs.trə.nɔːt", "A space traveler exploring the stars", "Astronauts float weightlessly in space stations.", "The astronaut walked on the moon's surface.", "Space traveler exploring stars"),
        WordTemplate("TELESCOPE", "Science", "🔭", "ˈtel.ə.skoʊp", "An instrument used to view distant stars", "Look through a telescope to see moon craters.", "We observed Saturn's rings with a telescope.", "Instrument viewing stars"),
        WordTemplate("SATELLITE", "Science", "🛰️", "ˈsæt.əl.aɪt", "A machine orbiting Earth sending signals", "Satellites transmit weather maps and signals.", "The shiny satellite orbited high above Earth.", "Space machine transmitting signals"),
        WordTemplate("COMPUTER", "Science", "💻", "kəmˈpjuː.tər", "An electronic machine for learning and games", "Computers help solve math and store stories.", "She typed a story on her computer.", "Electronic machine for work and play"),
        WordTemplate("KEYBOARD", "Technology", "⌨️", "ˈkiː.bɔːrd", "Buttons pressed to type words and numbers", "Press keys on a keyboard to write messages.", "He typed his name using the keyboard.", "Set of keys used for typing"),
        WordTemplate("MICROSCOPE", "Science", "🔬", "ˈmaɪ.krə.skoʊp", "A tool making tiny objects look big", "Microscopes reveal tiny cells and crystals.", "We examined leaf patterns under a microscope.", "Tool viewing tiny unseen objects"),
        WordTemplate("BACKPACK", "School", "🎒", "ˈbæk.pæk", "A bag worn on shoulders carrying school supplies", "Pack books and pencils in your backpack.", "She zipped up her colorful school backpack.", "Bag carried on shoulders"),
        WordTemplate("CLASSROOM", "School", "🏫", "ˈklæs.ruːm", "A room where students learn together", "Our classroom has books, maps and art.", "Bright artwork decorated the friendly classroom.", "Learning room for students"),
        WordTemplate("LIBRARY", "School", "📚", "ˈlaɪ.brer.i", "A quiet building filled with thousands of books", "Discover magical stories at your local library.", "We checked out adventure books from the library.", "Quiet building filled with books"),
        WordTemplate("DICTIONARY", "School", "📖", "ˈdɪk.ʃən.er.i", "A book listing word meanings and spellings", "Look up new words in a dictionary.", "A dictionary explains what words mean.", "Book of word definitions"),
        WordTemplate("BICYCLE", "Vehicles", "🚲", "ˈbaɪ.sɪ.kəl", "A two-wheeled vehicle moved by pedaling", "Riding a bicycle is fun exercise.", "She wore a helmet while riding her bicycle.", "Two-wheeled pedal vehicle"),
        WordTemplate("HELICOPTER", "Vehicles", "🚁", "ˈhel.ə.kɑːp.tər", "A flying vehicle with spinning overhead blades", "Helicopters can hover steadily in air.", "A rescue helicopter flew past the clouds.", "Aircraft with spinning blades"),
        WordTemplate("SUBMARINE", "Vehicles", "🌊", "ˈsʌb.mə.riːn", "A ship travelling deep underwater", "Submarines explore dark ocean trenches.", "The yellow submarine dived deep underwater.", "Underwater ocean vessel"),
        WordTemplate("HOVERCRAFT", "Vehicles", "🛥️", "ˈhʌv.ər.kræft", "A vehicle gliding over land and water on air", "Hovercrafts skim across mud and water waves.", "The hovercraft glided smoothly over sand.", "Vehicle gliding on cushion of air"),
        WordTemplate("SOCCER", "Sports", "⚽", "ˈsɑː.kər", "A popular game played by kicking a round ball", "Pass the soccer ball to score goals.", "The team cheered after scoring a soccer goal.", "Kicking ball team sport"),
        WordTemplate("BASKETBALL", "Sports", "🏀", "ˈbæs.kət.bɔːl", "A game where you bounce and shoot balls through hoops", "Dribble the orange basketball down the court.", "He shot a three-pointer in basketball.", "Game bouncing ball into hoop"),
        WordTemplate("SWIMMING", "Sports", "🏊", "ˈswɪm.ɪŋ", "Moving through water using arms and legs", "Swimming refreshes your body on hot days.", "She won a blue ribbon in swimming.", "Moving through water"),
        WordTemplate("GYMNASTICS", "Sports", "🤸", "dʒɪmˈnæs.tɪks", "Exercises displaying balance, flips and flexibility", "Gymnastics builds strength and coordination.", "She performed a flip in gymnastics class.", "Sport of flips and balance"),
        WordTemplate("MARATHON", "Sports", "🏃", "ˈmær.ə.θɑːn", "A long distance running race", "Runners train hard to complete a marathon.", "Cheering crowds clapped along the marathon route.", "Long distance running race"),
        WordTemplate("TRAMPOLINE", "Sports", "🪜", "ˈtræm.pə.liːn", "A bouncy spring frame for jumping high", "Jump high and perform safe bounces on a trampoline.", "Children laughed jumping on the trampoline.", "Springy mat for jumping high"),
        WordTemplate("WIZARD", "Fantasy", "🧙", "ˈwɪz.ərd", "A wise magician with magical spells", "Wizards wave magic staves and read ancient spellbooks.", "The wizard cast a glowing light spell.", "Wise magic spellcaster"),
        WordTemplate("MERMAID", "Fantasy", "🧜‍♀️", "ˈmɜːr.meɪd", "A mythical ocean creature half human half fish", "Mermaids swim beside friendly dolphins.", "A sparkling mermaid sat on an ocean rock.", "Mythical half-human half-fish"),
        WordTemplate("PHOENIX", "Fantasy", "🦅", "ˈfiː.nɪks", "A legendary bird rising from bright flames", "The phoenix symbolizes rebirth and hope.", "Gold feathers shone as the phoenix soared.", "Legendary fire bird"),
        WordTemplate("CRYSTAL", "Gems", "🔮", "ˈkrɪs.təl", "A shining natural mineral with geometric sides", "Crystals sparkle when light passes through.", "She found a shiny purple quartz crystal.", "Shining mineral gem"),
        WordTemplate("TREASURE", "Adventure", "💎", "ˈtreʒ.ər", "Valuable gems, gold and precious items", "The treasure chest was filled with gold coins.", "Pirates searched the island for buried treasure.", "Valuable gold chest"),
        WordTemplate("KINGDOM", "Fantasy", "🏰", "ˈkɪŋ.dəm", "A realm ruled by a benevolent king and queen", "Peace and happiness filled the fairytale kingdom.", "Travelers visited the mountain kingdom.", "Realm ruled by king and queen"),
        WordTemplate("LIGHTNING", "Weather", "⚡", "ˈlaɪt.nɪŋ", "A flash of electrical energy in the sky", "Lightning illuminates dark storm clouds.", "A bright flash of lightning lit the night.", "Flash of electricity in clouds"),
        WordTemplate("TORNADO", "Weather", "🌪️", "tɔːrˈneɪ.doʊ", "A spinning funnel wind storm", "Tornadoes spin with powerful wind forces.", "The meteorologist warned of a passing tornado.", "Spinning funnel wind storm"),
        WordTemplate("SUNSHINE", "Weather", "☀️", "ˈsʌn.ʃaɪn", "Warm golden rays from the bright sun", "Sunshine makes plants grow and flowers bloom.", "Morning sunshine filled the warm room.", "Warm rays from the sun"),
        WordTemplate("CONTINENT", "Geography", "🌍", "ˈkɑːn.tə.nənt", "A major landmass on planet Earth", "Asia and Africa are large continents.", "We learned about seven global continents.", "Major landmass on Earth"),
        WordTemplate("HORIZON", "Geography", "🌅", "həˈraɪ.zən", "The line where Earth's surface meets the sky", "Watch the sunset glow along the ocean horizon.", "The sun dipped below the blue horizon.", "Where earth meets the sky"),
        WordTemplate("HARBOR", "Geography", "⚓", "ˈhɑːr.bər", "A safe sheltered water area for ships", "Ships drop anchor in the quiet harbor.", "Fishing boats docked safely in the harbor.", "Sheltered water for ships"),
        WordTemplate("PIANO", "Arts", "🎹", "piˈæn.oʊ", "A musical instrument played with black and white keys", "Press keys smoothly to play soft piano tunes.", "She practiced a song on the grand piano.", "Keyed musical instrument"),
        WordTemplate("VIOLIN", "Arts", "🎻", "ˌvaɪ.əˈlɪn", "A wooden string instrument played with a bow", "Drawing a bow across violin strings plays melodies.", "The violinist played a graceful melody.", "String instrument played with bow"),
        WordTemplate("SAXOPHONE", "Arts", "🎷", "ˈsæk.sə.foʊn", "A brass wind instrument creating rich jazz sounds", "Saxophones produce warm smooth musical tones.", "He played a catchy jazz tune on saxophone.", "Brass wind instrument"),
        WordTemplate("PAINTING", "Arts", "🎨", "ˈpeɪn.tɪŋ", "A picture made with colorful paints", "Mix colors on a palette to make a painting.", "Her watercolor painting showed a quiet lake.", "Art created with paint"),
        WordTemplate("SCULPTURE", "Arts", "🗿", "ˈskʌlp.tʃər", "A 3D art figure shaped from stone or clay", "Sculptors carve marble into impressive statues.", "Visitors admired the museum sculpture.", "Art shaped from stone or clay"),
        WordTemplate("SYMPHONY", "Arts", "🎼", "ˈsɪm.fə.ni", "An elaborate musical piece for a full orchestra", "Violins and flutes united in a symphony.", "The orchestra performed a grand symphony.", "Elaborate orchestral music"),
        WordTemplate("BRILLIANT", "Qualities", "✨", "ˈbrɪl.jənt", "Exceptionally smart, clever or bright", "Solving hard puzzles shows a brilliant mind.", "She came up with a brilliant new idea.", "Exceptionally smart or bright"),
        WordTemplate("CREATIVE", "Qualities", "💡", "kriˈeɪ.tɪv", "Having imaginative artistic ideas", "Creative thinking inspires inventions and art.", "His creative story made everyone smile.", "Having imaginative ideas"),
        WordTemplate("CHAMPION", "Qualities", "🏆", "ˈtʃæm.pi.ən", "A winner who displays dedication and excellence", "Champions practice hard to achieve success.", "She held the trophy as spelling champion.", "Top winner in a contest"),
        WordTemplate("KNOWLEDGE", "Education", "🎓", "ˈnɑː.lɪdʒ", "Understanding and facts learned over time", "Reading books grows your store of knowledge.", "Learning spelling unlocks world knowledge.", "Understanding learned facts"),
        WordTemplate("DISCOVERY", "Science", "🔍", "dɪˈskʌv.ər.i", "Finding or learning something brand new", "Scientific discovery brings new medical breakthroughs.", "Exploring leads to exciting discovery.", "Finding something brand new")
    )

    private val levelList: List<WordLevel> by lazy {
        val list = mutableListOf<WordLevel>()
        list.addAll(base100Levels)

        val totalBankSize = extendedWordBank.size

        for (lvl in 101..1000) {
            val bankIndex = (lvl - 101) % totalBankSize
            val cycle = (lvl - 101) / totalBankSize
            val template = extendedWordBank[bankIndex]

            // If words wrap around past 1000, prefix with tier descriptor to make every level distinct
            val prefix = when (cycle) {
                0 -> ""
                1 -> "SUPER "
                2 -> "MEGA "
                3 -> "ROYAL "
                4 -> "MAGIC "
                5 -> "GOLDEN "
                6 -> "MASTER "
                else -> "ULTRA "
            }

            val rawWord = (prefix + template.word).trim()
            val cleanWord = rawWord.replace(" ", "")
            val len = cleanWord.length

            val calculatedDifficulty = when {
                lvl <= 150 -> 1
                lvl <= 300 -> if (len <= 4) 2 else 3
                lvl <= 500 -> if (len <= 5) 3 else 4
                lvl <= 800 -> if (len <= 6) 4 else 5
                else -> 5
            }

            val levelCategory = if (prefix.isNotEmpty()) "Master Tier" else template.category
            val levelMeaning = if (prefix.isNotEmpty()) "Advanced spelling challenge: ${template.meaning}" else template.meaning

            list.add(
                WordLevel(
                    id = lvl,
                    levelNumber = lvl,
                    word = cleanWord,
                    category = levelCategory,
                    categoryEmoji = template.emoji,
                    pronunciation = template.pronunciation,
                    meaning = levelMeaning,
                    simpleExplanation = template.simpleExplanation,
                    exampleSentence = template.exampleSentence,
                    hint = template.hint,
                    difficulty = calculatedDifficulty,
                    roundCount = 3
                )
            )
        }
        list
    }

    fun getLevel(levelNumber: Int): WordLevel {
        val safeNum = levelNumber.coerceIn(1, getTotalLevelCount())
        return levelList.getOrNull(safeNum - 1) ?: levelList.first()
    }

    fun getAllLevels(): List<WordLevel> = levelList

    fun getTotalLevelCount(): Int = 1000
}

