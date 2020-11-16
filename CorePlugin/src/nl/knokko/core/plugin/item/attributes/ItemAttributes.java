/*******************************************************************************
 * The MIT License
 *
 * Copyright (c) 2019 knokko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *  
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package nl.knokko.core.plugin.item.attributes;

import java.util.Collection;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ItemAttributes {
	
	public static class Single {
		
		private final String attribute;
		private final String slot;
		private final int operation;
		
		private final double value;
		
		public Single(String attribute, String slot, int operation, double value) {
			this.attribute = attribute;
			this.slot = slot;
			this.operation = operation;
			this.value = value;
		}
		
		public boolean isDummy() {
			return operation == 0 && value == 0;
		}
		
		@Override
		public String toString() {
			return "ItemAttributes.Single(" + attribute + "," + slot + "," 
					+ operation + "," + value + ")";
		}
		
		@Override
		public boolean equals(Object other) {
			if (other instanceof Single) {
				Single single = (Single) other;
				return single.attribute.equals(attribute) && Objects.equals(slot, single.slot)
						&& single.operation == operation && single.value == value;
			} else {
				return false;
			}
		}
	}
	
	public static class Slot {
		
		public static final String MAIN_HAND = "mainhand";
		public static final String OFF_HAND = "offhand";
		
		public static final String HELMET = "head";
		public static final String CHESTPLATE = "chest";
		public static final String LEGGINGS = "legs";
		public static final String BOOTS = "feet";
	}
	
	public static class Operation {
		
		public static final int ADD = 0;
		public static final int MULTIPLY = 1;
		public static final int CHAIN_MULTIPLY = 2;
	}
	
	public static class Attributes {
		
		public static final String ATTACK_DAMAGE = "generic.attackDamage";
		public static final String ATTACK_SPEED = "generic.attackSpeed";
		public static final String MAX_HEALTH = "generic.maxHealth";
		public static final String MOVEMENT_SPEED = "generic.movementSpeed";
		public static final String KNOCKBACK_RESISTANCE = "generic.knockbackResistance";
		public static final String LUCK = "generic.luck";
		public static final String ARMOR = "generic.armor";
		public static final String ARMOR_TOUGHNESS = "generic.armorToughness";
	}
	
	private static Attribute toBukkitAttribute(String attributeName) {
		return Attribute.valueOf(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, attributeName).replace('.', '_'));
	}
	
	private static String fromBukkitAttribute(Attribute attribute) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, attribute.name().replaceFirst("_", "."));
	}
	
	private static EquipmentSlot toBukkitSlot(String slot) {
		if (slot == null) {
			return null;
		}
		if (slot.equals(Slot.MAIN_HAND)) {
			return EquipmentSlot.HAND;
		} else if (slot.equals(Slot.OFF_HAND)){
			return EquipmentSlot.OFF_HAND;
		} else {
			return EquipmentSlot.valueOf(slot.toUpperCase(Locale.ROOT));
		}
	}
	
	private static String fromBukkitSlot(EquipmentSlot slot) {
		if (slot == null) {
			return null;
		}
		if (slot == EquipmentSlot.HAND) {
			return Slot.MAIN_HAND;
		} else if (slot == EquipmentSlot.OFF_HAND) {
			return Slot.OFF_HAND;
		} else {
			return slot.name().toLowerCase(Locale.ROOT);
		}
	}
	
	private static int slotHashCode(String slot) {
		if (slot == null) {
			return 111;
		} else {
			return slot.hashCode();
		}
	}
	
	private static AttributeModifier toBukkitAttributeModifier(Single attribute, int index) {
		long most = index + 1 + slotHashCode(attribute.slot) * attribute.attribute.hashCode();
		long least = index + 1 + slotHashCode(attribute.slot) + attribute.attribute.hashCode();
		if (most == 0) most = -8;
		if (least == 0) least = 12;
		return new AttributeModifier(new UUID(most, least), attribute.attribute, attribute.value,
				AttributeModifier.Operation.values()[attribute.operation], toBukkitSlot(attribute.slot));
	}
	
	// The parameters are just magic numbers, hoping to avoid collisions
	private static final UUID DUMMY_UUID = new UUID(39847328746L, -2742859264376L);
	
	private static void addDummyAttributeModifier(ItemMeta meta) {
		meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(DUMMY_UUID, "dummy", 0, AttributeModifier.Operation.ADD_NUMBER));
	}
	
	public static ItemStack createWithAttributes(String materialName, int amount, Single...attributes) {
		return createWithAttributes(Material.valueOf(materialName), amount, attributes);
	}
	
	public static ItemStack createWithAttributes(Material type, int amount, Single...attributes) {
		ItemStack original = new ItemStack(type, amount);
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(type);
		if (attributes.length == 0) {
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			addDummyAttributeModifier(meta);
			original.setItemMeta(meta);
			return original;
		} else {
			for (int index = 0; index < attributes.length; index++) {
				Single attribute = attributes[index];
				meta.addAttributeModifier(toBukkitAttribute(attribute.attribute), toBukkitAttributeModifier(attribute, index));
			}
			original.setItemMeta(meta);
			return original;
		}
	}
	
	public static ItemStack replaceAttributes(ItemStack original, Single...attributes) {
		ItemMeta meta = original.getItemMeta();
		if (meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(original.getType());
		} else {
			for (Attribute attribute : meta.getAttributeModifiers().keySet()) {
				meta.removeAttributeModifier(attribute);
			}
		}
		for (int index = 0; index < attributes.length; index++) {
			Single attribute = attributes[index];
			meta.addAttributeModifier(toBukkitAttribute(attribute.attribute), toBukkitAttributeModifier(attribute, index));
		}
		if (attributes.length == 0) {
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			addDummyAttributeModifier(meta);
		}
		original.setItemMeta(meta);
		return original;
	}
	
	public static ItemStack setAttributes(ItemStack original, Single...attributes) {
		ItemMeta meta = original.getItemMeta();
		if (meta == null) {
			meta = Bukkit.getItemFactory().getItemMeta(original.getType());
		}
		for (int index = 0; index < attributes.length; index++) {
			Single attribute = attributes[index];
			meta.addAttributeModifier(toBukkitAttribute(attribute.attribute), toBukkitAttributeModifier(attribute, index));
		}
		original.setItemMeta(meta);
		return original;
	}
	
	/**
	 * This method name is actually horrible, the name addAttribute would make much more sense, but I changing
	 * that would break other plug-ins.
	 */
	public static ItemStack setAttribute(ItemStack original, String attribute, double value, String slot, int operation) {
		ItemMeta meta = original.getItemMeta();
		try {
			if (meta == null) {
				meta = Bukkit.getItemFactory().getItemMeta(original.getType());
			}
			int id = 0;
			if (meta.hasAttributeModifiers()) {
				id = meta.getAttributeModifiers().size();
			}
			meta.addAttributeModifier(toBukkitAttribute(attribute), toBukkitAttributeModifier(new Single(attribute, slot, operation, value), id));
		} catch (IllegalArgumentException invalidAttribute) {
			meta.setDisplayName("Invalid attribute: " + attribute);
		}
		original.setItemMeta(meta);
		return original;
	}
	
	public static double getAttribute(ItemStack stack, String attribute) {
		ItemMeta meta = stack.getItemMeta();
		if (meta != null) {
			try {
				Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(toBukkitAttribute(attribute));
				if (modifiers != null) {
					for (AttributeModifier modifier : modifiers) {
						return modifier.getAmount();
					}
				}
			} catch (IllegalArgumentException noSuchAttribute) {
				// This will happen when the user asked for an invalid attribute
				// Just return NaN to indicate it doesn't exist
			}
		}
		return Double.NaN;
	}
	
	public static Single[] getAttributes(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if (meta != null) {
			Multimap<Attribute, AttributeModifier> attributeModifiers = meta.getAttributeModifiers();
			if (attributeModifiers == null) {
				return new Single[0];
			}
			
			Single[] attributes = new Single[attributeModifiers.size()];
			int index = 0;
			for (Entry<Attribute, AttributeModifier> attributePair : attributeModifiers.entries()) {
				String attribute = fromBukkitAttribute(attributePair.getKey());
				String slot = fromBukkitSlot(attributePair.getValue().getSlot());
				int operation = attributePair.getValue().getOperation().ordinal();
				double value = attributePair.getValue().getAmount();
				attributes[index] = new Single(attribute, slot, operation, value);
				index++;
			}
			
			return attributes;
		} else {
			return new Single[0];
		}
	}
	
	public static ItemStack clearAttributes(ItemStack original) {
		ItemMeta meta = original.getItemMeta();
		if (meta != null) {
			meta.setAttributeModifiers(ArrayListMultimap.create());
		} else {
			meta = Bukkit.getItemFactory().getItemMeta(original.getType());
		}
		addDummyAttributeModifier(meta);
		original.setItemMeta(meta);
		return original;
	}
	
	public static ItemStack resetAttributes(ItemStack original) {
		ItemMeta meta = original.getItemMeta();
		if (meta != null) {
			meta.setAttributeModifiers(new ItemStack(original.getType()).getItemMeta().getAttributeModifiers());
			original.setItemMeta(meta);
		}
		return original;
	}
	
	public static String[] listAttributes(ItemStack stack) {
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) return null;
		Multimap<Attribute, AttributeModifier> modifiers = meta.getAttributeModifiers();
		if (modifiers == null) return null;
		String[] result = new String[modifiers.size()];
		Collection<Entry<Attribute,AttributeModifier>> entries = modifiers.entries();
		int index = 0;
		for (Entry<Attribute,AttributeModifier> entry : entries) {
			result[index++] = fromBukkitAttribute(entry.getKey()) + ": " + entry.getValue().getAmount();
		}
		return result;
	}
}